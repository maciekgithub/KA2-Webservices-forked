CREATE TABLE requests(
  p_request_url VARCHAR2(255),
  p_request_body CLOB,
  p_error_code NUMBER,
  p_answer CLOB
);

CREATE OR REPLACE PACKAGE wbs_services
AS
  PROCEDURE request(p_request_url IN VARCHAR2, p_request_body IN CLOB, p_error_code OUT NUMBER, p_answer OUT CLOB);
END wbs_services;

CREATE OR REPLACE PACKAGE BODY wbs_services
AS
PROCEDURE request(p_request_url IN VARCHAR2, p_request_body IN CLOB, p_error_code OUT NUMBER, p_answer OUT CLOB)
IS
  BEGIN
    p_answer := '{"name": "Przemek"}';
    p_error_code := 200;
    INSERT INTO requests VALUES (p_request_url, p_request_body, p_error_code, p_answer);
  END;
END wbs_services;

SELECT * from requests;
--DROP TABLE requests;


DECLARE
  answer CLOB;
  status INTEGER;
BEGIN
  wbs_services.request('/api/metrics', '{"name": "Robert"}', status, answer);
END;

-- check user open sessions
select username, sid, serial# from v$session where username = 'BGS';