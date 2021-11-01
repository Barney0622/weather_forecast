SELECT location.lname, element.ename, time.st, time.et, record.parameter
FROM ((record join location 
on record.lid = location.lid)
join time
on record.tid = time.tid)
join element
on record.eid = element.eid
ORDER BY location.lid, element.eid
;