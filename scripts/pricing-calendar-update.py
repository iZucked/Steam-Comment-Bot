import os
import sys
import datetime
import calendar

def row_comarator_key(row):
    exchange_name, _, _, _, month_numeric_notation = row
    return exchange_name, month_numeric_notation

years_to_append = [2023, 2024]

FIRST_DAY_OF_THE_MONTH = 1
LAST_DAY_OF_THE_MONTH = -2

calendars = [
    {
        "name": "JKM",
        "title": "JKM Calendar",
        "start": "15 month-1",
        "end": "14 month"
    },
    {
        "name": "ICE",
        "title": "ICE Calendar",
        "start": "FIRST_DAY_OF_THE_MONTH month-1",
        "end": "LAST_DAY_OF_THE_MONTH month-1"
    },
    {
        "name": "Socal",
        "title": "HH and Socal calendars",
        "start": "LAST_DAY_OF_THE_MONTH month-1",
        "end": "LAST_DAY_OF_THE_MONTH month-1"
    },
]

print('Searching Pricing Calendar CSV in the project...', flush=True)

filename = 'Pricing Calendars.csv'
data_dir_path = os.path.join(
    os.path.dirname(os.path.abspath(__name__)),
    '..',
    'com-mmxlabs-lngdataserver',
    'com.mmxlabs.lngdataserver.lng.importers',
    'data'
)

final_path = os.path.normpath(os.path.join(data_dir_path, filename))

print('Path initialized correctly! Reading file...', flush=True)

rows = []

header = ""
with open(final_path) as f:
    for line in f.readlines():
        if header == "":
            header = line
        else:
            row = line.strip().split(',')
            rows.append(row)

print('File was read successfully! Adding new data...', flush=True)

for exchange_calendar in calendars:

    exchange_name = exchange_calendar['name']

    # rows.append([exchange_name, exchange_calendar['title'], '', '', ''])

    for year in years_to_append:
        for month in range(1, 13):

            month_name = datetime.date(year, month, 1).strftime("%B").lower().capitalize()

            start_day, start_month= tuple(map(eval, exchange_calendar['start'].split()))
            end_day, end_month = tuple(map(eval, exchange_calendar['end'].split()))
            start_year = year
            end_year = year

            if start_month < 1:
                start_month += 12
                start_year -= 1
            if end_month < 1:
                end_month += 12
                end_year -= 1

            if end_day == LAST_DAY_OF_THE_MONTH:
                _, end_day = calendar.monthrange(year, end_month)
            if start_day == LAST_DAY_OF_THE_MONTH:
                _, start_day = calendar.monthrange(year, end_month)

            start_date = f'{"%02d" % (start_day,)}/{"%02d" % (start_month,)}/{start_year}'
            end_date = f'{"%02d" % (end_day,)}/{"%02d" % (end_month,)}/{end_year}'

            month_numeric_notation = f'{year}-{"%02d" % (month,)}'

            rows.append([exchange_name, month_name, start_date, end_date, month_numeric_notation])

rows = sorted(rows, key=row_comarator_key)

print('Data sorted! Writing it back...', flush=True)

with open(final_path, 'w') as f:
    f.write(header)
    for row in rows:
        f.write(','.join(row) + '\n')

print('OK!', flush=True)
