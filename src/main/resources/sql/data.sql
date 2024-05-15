SELECT jsonb_array_elements(data) ->> 'id'    AS id,
       jsonb_array_elements(data) ->> 'key'   AS key,
       jsonb_array_elements(data) ->> 'value' AS value
FROM excel_file_json_tb;

SELECT data ->> 'id' AS id, data ->> 'key' AS key, data ->> 'value' AS value
FROM excel_file_json_tb;

