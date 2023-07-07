import os
import datetime

SUNDAY = 6
SATURDAY = 5

def is_not_weekend(date):
    yyyy, mm, dd = list(map(int, date.split('-')))
    return datetime.datetime(yyyy, mm, dd).weekday() not in [SATURDAY, SUNDAY]

def if_andrey_machine_then_ensure_that(cond, msg=""):
    debug_condition = os.environ['username'] == 'Andre'
    if debug_condition:
        assert cond, msg


print('Searching Holiday Calendar CSV in the project...')

filename = 'Holiday Calendars.csv'
data_dir_path = os.path.join(
    os.path.dirname(os.path.abspath(__name__)),
    '..',
    'com-mmxlabs-lngdataserver',
    'com.mmxlabs.lngdataserver.lng.importers',
    'data'
)

final_path = os.path.normpath(os.path.join(data_dir_path, filename))

#
#  SANITY CHECK
#
if_andrey_machine_then_ensure_that(
    str(final_path) == 'C:\ws\lingo1\ws\lingo.git\com-mmxlabs-lngdataserver\com.mmxlabs.lngdataserver.lng.importers\data\Holiday Calendars.csv',
    str(final_path)
)

print('path initialized correctly! Reading file...')

good_lines = []

with open(final_path) as f:
    for line in f.readlines():
        
        row = line.strip().split(',')

        #
        #  Headers or subheaders
        #
        if (row[-1] in ['', 'date']):
            good_lines.append(line)
            continue

        date = row[-1]

        if is_not_weekend(date):
            good_lines.append(line)
        else:
            print('\tGot rid of date', date)

print('File was read successfully! Rewriting data back...')

with open(final_path, 'w') as f:
    for line in good_lines:
        f.write(line)
