CREATE VIEW damage_report_summary AS
WITH summary AS (SELECT CAST(SUM(apas.min_price) AS DECIMAL(10, 2))                   AS estimated_min_price_without_service,
                        CAST(SUM(apas.max_price) AS DECIMAL(10, 2))                   AS estimated_max_price_without_service,
                        CAST(SUM(apas.min_price + apasc.min_price) AS DECIMAL(10, 2)) AS estimated_min_price_with_service,
                        CAST(SUM(apas.max_price + apasc.max_price) AS DECIMAL(10, 2)) AS estimated_max_price_with_service,
                        drd.report_id
                 FROM damage_report_data drd
                          JOIN auto_parts_and_services apas ON drd.auto_part_service_id = apas.id
                          JOIN public.auto_parts_and_services_categories apasc on apas.category_id = apasc.id
                 group by drd.report_id)
SELECT dr.report_id,
       u.full_name,
       s.estimated_min_price_without_service,
       s.estimated_min_price_with_service,
       s.estimated_max_price_without_service,
       s.estimated_max_price_with_service
FROM damage_reports dr
         LEFT JOIN summary s ON dr.report_id = s.report_id
         LEFT JOIN users u ON dr.user_id = u.id;