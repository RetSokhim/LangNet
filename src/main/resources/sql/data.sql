-- SELECT jsonb_array_elements(data) ->> 'id'    AS id,
--        jsonb_array_elements(data) ->> 'key'   AS key,
--        jsonb_array_elements(data) ->> 'value' AS value
-- FROM excel_file_json_tb;
--
-- SELECT data ->> 'id' AS id, data ->> 'key' AS key, data ->> 'value' AS value
-- FROM excel_file_json_tb;


SELECT role_tb.role_name FROM role_tb
JOIN public.project_member_tb pmt on role_tb.role_id = pmt.role_id
WHERE user_id = '67a24656-c68e-4f00-ae32-e8cd675b866b' AND project_id = 'af76aa09-8ffc-4f8a-b623-cd1a623d4515';

SELECT *
FROM project_tb p
         INNER JOIN project_member_tb pm ON p.project_id = pm.project_id
         INNER JOIN user_tb u ON pm.user_id = u.user_id
         INNER JOIN role_tb r ON pm.role_id = r.role_id
WHERE p.project_name = 'string';

SELECT *
FROM user_tb u
         JOIN project_member_tb pm ON u.user_id = pm.user_id
         JOIN role_tb r ON pm.role_id = r.role_id
         LEFT JOIN contact_tb c ON u.user_id = c.user_id
WHERE pm.project_id = 'e779e4a0-98cc-48f8-b05d-f0898d650f00';


