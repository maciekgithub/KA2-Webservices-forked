CREATE TABLE BGS_WEBSERVICES.WBS$AGENTS
(
  "AGENT_NAME"    VARCHAR2(30 BYTE)  DEFAULT NULL,
  "ENABLED"       VARCHAR2(1 BYTE)   DEFAULT 'Y',
  "PARAM_NAME"    VARCHAR2(30 BYTE)  DEFAULT NULL,
  "DEFAULT_URL"   VARCHAR2(200 BYTE) DEFAULT NULL,
  "OUTGOING_SSL"  VARCHAR2(1 BYTE)   DEFAULT 'N',
  "INCOMING_SSL"  VARCHAR2(1 BYTE)   DEFAULT 'N',
  "AUTH_PASSWORD" VARCHAR2(50 BYTE)  DEFAULT NULL
);

CREATE TABLE "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL"
(
  "REQUEST_TYPE" VARCHAR2(30 BYTE) DEFAULT NULL,
  "AGENT_NAME"   VARCHAR2(30 BYTE) DEFAULT NULL,
  "ENABLED"      VARCHAR2(1 BYTE)  DEFAULT 'Y'
);

CREATE TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS"
(
  "REQUEST_TYPE"   VARCHAR2(30 BYTE)  DEFAULT NULL,
  "REQUEST_METHOD" VARCHAR2(30 BYTE)  DEFAULT NULL,
  "ENABLED"        VARCHAR2(1 BYTE)   DEFAULT 'Y',
  "URL"            VARCHAR2(100 BYTE) DEFAULT NULL,
  "PROCEDURE_NAME" VARCHAR2(100 BYTE) DEFAULT NULL
);

CREATE TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS"
(
  "REQUEST_TYPE"   VARCHAR2(30 BYTE)   DEFAULT NULL,
  "AGENT_NAME"     VARCHAR2(30 BYTE)   DEFAULT NULL,
  "PARAM_NAME"     VARCHAR2(30 BYTE)   DEFAULT NULL,
  "PARAM_USE_TYPE" VARCHAR2(30 BYTE)   DEFAULT NULL,
  "PARAM_VALUE"    VARCHAR2(4000 BYTE) DEFAULT NULL
);

CREATE UNIQUE INDEX "BGS_WEBSERVICES"."WBS$AGENTS_PK" ON "BGS_WEBSERVICES"."WBS$AGENTS" ("AGENT_NAME");
CREATE UNIQUE INDEX "BGS_WEBSERVICES"."WBS$AGENTS_UK1" ON "BGS_WEBSERVICES"."WBS$AGENTS" ("PARAM_NAME");
CREATE UNIQUE INDEX "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL_PK" ON "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" ("REQUEST_TYPE", "AGENT_NAME");
CREATE INDEX "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL_FK1" ON "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" ("REQUEST_TYPE");
CREATE INDEX "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL_FK2" ON "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" ("AGENT_NAME");
CREATE UNIQUE INDEX "BGS_WEBSERVICES"."WBS$ENDPOINTS_PK" ON "BGS_WEBSERVICES"."WBS$ENDPOINTS" ("REQUEST_TYPE");
CREATE INDEX "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS_FK1" ON "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS" ("REQUEST_TYPE", "AGENT_NAME");

ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" MODIFY ("AGENT_NAME" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" MODIFY ("ENABLED" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" MODIFY ("PARAM_NAME" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" MODIFY ("DEFAULT_URL" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" MODIFY ("OUTGOING_SSL" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" MODIFY ("INCOMING_SSL" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" ADD CONSTRAINT "WBS$AGENTS_CHK1" CHECK (enabled IN ('Y', 'N')) ENABLE;
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" ADD CONSTRAINT "WBS$AGENTS_CHK2" CHECK (outgoing_ssl IN ('Y', 'N')) ENABLE;
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" ADD CONSTRAINT "WBS$AGENTS_CHK3" CHECK (incoming_ssl IN ('Y', 'N')) ENABLE;
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" ADD CONSTRAINT "WBS$AGENTS_PK" PRIMARY KEY ("AGENT_NAME");
ALTER TABLE "BGS_WEBSERVICES"."WBS$AGENTS" ADD CONSTRAINT "WBS$AGENTS_UK1" UNIQUE ("PARAM_NAME");
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" MODIFY ("REQUEST_TYPE" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" MODIFY ("AGENT_NAME" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" MODIFY ("ENABLED" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" ADD CONSTRAINT "WBS$ENDPOINT_ACL_CHK1" CHECK (enabled IN ('Y', 'N')) ENABLE;
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINT_ACL" ADD CONSTRAINT "WBS$ENDPOINT_ACL_PK" PRIMARY KEY ("REQUEST_TYPE", "AGENT_NAME");
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" MODIFY ("REQUEST_TYPE" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" MODIFY ("REQUEST_METHOD" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" MODIFY ("ENABLED" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" MODIFY ("URL" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" MODIFY ("PROCEDURE_NAME" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" ADD CONSTRAINT "WBS$ENDPOINTS_CHK1" CHECK (enabled IN ('Y', 'N')) ENABLE;
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" ADD CONSTRAINT "WBS$ENDPOINTS_CHK2" CHECK (request_method IN ('get', 'put', 'post', 'delete')) ENABLE;
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS" ADD CONSTRAINT "WBS$ENDPOINTS_PK" PRIMARY KEY ("REQUEST_TYPE") ;
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS" MODIFY ("REQUEST_TYPE" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS" MODIFY ("AGENT_NAME" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS" MODIFY ("PARAM_NAME" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS" MODIFY ("PARAM_USE_TYPE" NOT NULL ENABLE);
ALTER TABLE "BGS_WEBSERVICES"."WBS$ENDPOINTS_PARAMS" ADD CONSTRAINT "WBS$ENDPOINTS_PARAMS_CHK1" CHECK (param_use_type IN ('REQUIRED', 'FORBIDDEN', 'DEFAULT')) ENABLE;