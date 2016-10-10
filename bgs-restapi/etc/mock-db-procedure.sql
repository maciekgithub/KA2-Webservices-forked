CREATE TABLE requests(
  p_request_url VARCHAR2(255),
  p_request_body CLOB,
  p_answer CLOB,
  p_status INTEGER
);

CREATE OR REPLACE PROCEDURE request(p_request_url IN VARCHAR2, p_request_body IN CLOB, p_answer OUT CLOB, p_status OUT INTEGER)
IS
  BEGIN
    p_answer := '{"name": "Przemek"}';
    p_status := 200;
    INSERT INTO requests VALUES (p_request_url, p_request_body, p_answer, p_status);
  END;

SELECT * from requests;
--DROP TABLE requests;


DECLARE
  answer CLOB;
  status INTEGER;
BEGIN
  request('/api/metrics', '{"name": "Robert"}', answer, status);
END;