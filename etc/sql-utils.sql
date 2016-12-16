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

-- test execution
DECLARE
  answer CLOB;
  status INTEGER;
BEGIN
  bgs_webservices.wbs_webservices.request('/api/metrics', 'GET', '', 'agent', 'date', 'contenttype', '{"name": "Robert"}', status, answer);
END;