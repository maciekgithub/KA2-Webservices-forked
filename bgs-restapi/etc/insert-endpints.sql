
CREATE TABLE bgs_webservices.wbs_endpoints (
  request_type varchar2(255),
  request_method varchar2(255),
  url varchar2(255)
);

DELETE FROM BGS_WEBSERVICES.wbs_endpoints;

insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('getDictionary','get','/dictionaries');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('notIMEI','post','/imei');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('lstAbolist','get','/lists');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('getMetric','post','/lists');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('delAbolist','delete','/lists/{abonent_listname}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('getAbolistSimple','get','/lists/{abonent_listname}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('getAbolistFull','post','/lists/{abonent_listname}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('creAbolist','put','/lists/{abonent_listname}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('lstWatchdog','get','/wg');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('creWatchdog','post','/wg');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('delWatchdog','delete','/wg/{wg_name}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('infoWatchdog','get','/wg/{wg_name}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('updWatchdog','post','/wg/{wg_name}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('startWatchdog','put','/wg/{wg_name}/start');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('stopWatchdog','put','/wg/{wg_name}/stop');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('updSubscriptionList','post','/wg/{wg_name}/subscriptions');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('delSubscription','delete','/wg/{wg_name}/subscriptions/{msisdn}');
insert into bgs_webservices.wbs_endpoints(request_type, request_method, url) values('creSubscription','post','/wg/{wg_name}/subscriptions/{msisdn}');

select * from BGS_WEBSERVICES.wbs_endpoints;