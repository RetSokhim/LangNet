-- SELECT jsonb_array_elements(data) ->> 'id'    AS id,
--        jsonb_array_elements(data) ->> 'key'   AS key,
--        jsonb_array_elements(data) ->> 'value' AS value
-- FROM excel_file_json_tb;
--
-- SELECT data ->> 'id' AS id, data ->> 'key' AS key, data ->> 'value' AS value
-- FROM excel_file_json_tb;


SELECT role_tb.role_name FROM role_tb
JOIN public.project_member_tb pmt on role_tb.role_id = pmt.role_id
WHERE user_id = '67a24656-c68e-4f00-ae32-e8cd675b866b' AND project_id = 'af76aa09-8ffc-4f8a-b623-cd1a623d4515'

