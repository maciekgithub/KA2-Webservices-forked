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

-- view for all active endpoints
CREATE OR REPLACE VIEW bgs_webservices.wbs_endpoints(request_type, request_method, url)
AS SELECT "REQUEST_TYPE", "REQUEST_METHOD", "URL" FROM "BGS_WEBSERVICES"."WBS$ENDPOINTS" WHERE ENABLED = 'Y';

DELETE FROM bgs_webservices.wbs$endpoints;

insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('notIMEI','post','/api/imei', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('lstAbolist','get','/api/lists', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('delAbolist','delete','/api/lists/{abonent_listname}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('getAbolistSimple','get','/api/lists/{abonent_listname}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('getAbolistFull','post','/api/lists/{abonent_listname}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('creAbolist','put','/api/lists/{abonent_listname}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('lstWatchdog','get','/api/wg', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('creWatchdog','post','/api/wg', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('delWatchdog','delete','/api/wg/{wg_name}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('infoWatchdog','get','/api/wg/{wg_name}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('updWatchdog','post','/api/wg/{wg_name}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('startWatchdog','put','/api/wg/{wg_name}/start', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('stopWatchdog','put','/api/wg/{wg_name}/stop', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('updSubscriptionList','post','/api/wg/{wg_name}/subscriptions', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('delSubscription','delete','/api/wg/{wg_name}/subscriptions/{msisdn}', '-');
insert into bgs_webservices.wbs$endpoints(request_type, request_method, url, procedure_name) values('creSubscription','post','/api/wg/{wg_name}/subscriptions/{msisdn}', '-');
INSERT INTO BGS_WEBSERVICES.WBS$ENDPOINTS (REQUEST_TYPE, REQUEST_METHOD, URL, PROCEDURE_NAME) VALUES ('getMetric', 'post', '/api/lists', 'bgs_metrics.met_metrics_api.wsGetMetric');
INSERT INTO BGS_WEBSERVICES.WBS$ENDPOINTS (REQUEST_TYPE, REQUEST_METHOD, URL, PROCEDURE_NAME) VALUES ('getDictionary', 'get', '/api/dictionaries', 'bgs_metrics.met_metrics_api.getDictionary');

select * from bgs_webservices.wbs$endpoints;
-- example data, password for agents md5: wbs_agent
INSERT INTO BGS_WEBSERVICES.WBS$AGENTS (AGENT_NAME, ENABLED, PARAM_NAME, DEFAULT_URL, OUTGOING_SSL, INCOMING_SSL, AUTH_PASSWORD)
    VALUES ('BGS-WS', 'Y', 'webservices.net.bgs-ws', '/testnotification', 'N', 'N', 'd8d6d3fafb8a3154bf601c946d262f4a');
INSERT INTO BGS_WEBSERVICES.WBS$AGENTS (AGENT_NAME, ENABLED, PARAM_NAME, DEFAULT_URL, OUTGOING_SSL, INCOMING_SSL, AUTH_PASSWORD)
    VALUES ('GEN', 'Y', 'webservices.net.gen', '/', 'N', 'N', 'd8d6d3fafb8a3154bf601c946d262f4a');

INSERT INTO BGS_WEBSERVICES.WBS$ENDPOINT_ACL (REQUEST_TYPE, AGENT_NAME, ENABLED) VALUES ('getMetric', 'GEN', 'Y');
INSERT INTO BGS_WEBSERVICES.WBS$ENDPOINT_ACL (REQUEST_TYPE, AGENT_NAME, ENABLED) VALUES ('getDictionary', 'GEN', 'Y');

select * from WBS$AGENTS;
select * from WBS$ENDPOINTS;
select * from WBS$ENDPOINT_ACL;

delete from WBS$ENDPOINT_ACL;
delete from WBS$AGENTS;
delete from WBS$ENDPOINTS;