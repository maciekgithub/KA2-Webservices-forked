## How to create required certificates step-by-step

#### 1. Create the Root Certificate (Certificate Authority)

* create the root key

	```openssl genrsa -out rootCA.key 2048```

* self-sign the certificate (fill the required information about organization)

	```openssl req -x509 -new -nodes -key rootCA.key -sha256 -days 7300 -out rootCA.pem```
    
    Above command will create certificate called `rootCA.pem`
    
#### 2. Create child certificate and sign by created CA

* create a keystore file and generate a key pair
 
      keytool -genkeypair -keysize 2048 -keyalg RSA -alias bgs-server-key –keystore server.jks

* enter required information about certificate, please remember that `CN` needs to be a host name / ip address in order to avoid the problems with untrasted certyficate issues

* check that keystore was created

      keytool -list -keystore server.jks
    
* generate the certificate signing request (CSR) using newly created keystore

      keytool -certreq -alias bgs-server-key -keyalg RSA -file server.csr -keystore server.jks
    
* sign the CSR by created CA (this step will generate signed server.crt certificate)

        openssl x509 -req -in server.csr -CA rootCA.pem -CAkey rootCA.key -CAcreateserial -out server.crt -days 7300 -sha256
    
* import the new CA-signed certificate into the keystore (the root CA certificate first and signed by CA certificate last)

		keytool -import -alias root -trustcacerts -file root.pem -keystore server.jks
		keytool -import -alias bgs-server-key -trustcacerts -file server.crt -keystore server.jks

* replace old keystore used be application