import sys
import csv

csv.field_size_limit(sys.maxsize)

with open('offline-alignments.csv') as csv_file:
    csv_reader = csv.DictReader(csv_file, delimiter=",")
    line_count = 0
    for row in csv_reader:
        # if line_count > 4 and line_count < 235:
        case_ids = row["Case IDs"]
        raw = row["Raw Fitness Cost"]
        trace = row["Trace Fitness"]
        for case_id in case_ids.split("|"):
            print(case_id + ";" + raw + ";" + trace)
        line_count += 1
        