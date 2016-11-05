-- check user open sessions
select username, sid, serial# from v$session where username = 'BGS';

-- kill all session on server side
begin
  for x in (select  SID
              ,Serial#
              ,program
              ,username
            from v$session
            where username = 'BGS'
  ) loop
    execute immediate 'Alter System Kill Session ' || x.Sid || ',' || x.Serial# || ' IMMEDIATE';
  end loop;
end;
/

--- 2016.10.19 specyfikacja metod

create or replace package bgs_webservices.wbs_webservices is
  procedure request(
    p_request_url         varchar2,
    p_request_method      varchar2,
    p_request_urlparams   varchar2,
    p_header_agent        varchar2,
    p_header_date         varchar2,
    p_header_contenttype  VARCHAR2,
    p_request_body        clob,
    p_error_code          out number,
    p_answer_body         out clob
  );
end;

create or replace package body bgs_webservices.wbs_webservices is
  procedure request(
    p_request_url  varchar2,
    p_request_method varchar2,
    p_request_urlparams varchar2,
    p_header_agent varchar2,
    p_header_date  varchar2,
    p_header_contenttype VARCHAR2,
    p_request_body clob,
    p_error_code   out number,
    p_answer_body       out clob
  ) is
    begin
      sys.dbms_lock.sleep(10);
      p_error_code := 200; -- HTTP_OK
      p_answer_body := ' { "response": "OK" }';
    end;
end;

-- test execution

DECLARE
  answer CLOB;
  status INTEGER;
BEGIN
  bgs_webservices.wbs_webservices.request('/api/metrics', 'GET', '', 'agent', 'date', 'contenttype', '{"name": "Robert"}', status, answer);
END;