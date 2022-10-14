# check-my-bus
An Android app to check Hong Kong bus routes and estimated time of arrival (ETA) at each stop, using data from [Kowloon Motor Bus Company (1933) Limited (KMB) (incorporating Long Win Bus Company Limited (LWB))](https://data.gov.hk/en-data/dataset/hk-td-tis_21-etakmb), [Citybus Limited (CTB)](https://data.gov.hk/en-data/dataset/ctb-eta-transport-realtime-eta) and [New World First Bus Services Limited (NWFB)](https://data.gov.hk/en-data/dataset/nwfb-eta-transport-realtime-eta) under the [OpenAPI initiative of the Hong Kong Government](https://data.gov.hk/en/).

## How to use
1. Clone the repository into a local directory.
2. Open the project directory in Android Studio.

## APIs called
### 1. Route API
In MainActivity, to look up the routes in each company.

### 2. Route-Stop API
In MainActivity, to determine whether the route is circular (no inbound stops).

In SelectStopActivity, to get all stop IDs of the selected route.

### 3. Stop API
In SelectStopActivity, to get the stop name, location (latitude and longitude).

### 4. Stop-ETA API
In SelectTimeActivity, to get the ETA of selected stop.

## Description
The app consists of 3 Activities, namely:

1. MainActivity
2. SelectStopActivity
3. SelectTimeActivity

### 1. MainActivity
<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/main_activity_init.png" alt="MainActivity Initial Screen" width="25%" height="25%">

#### Normal routes
The initial screen, allowing users to input route number in EditText. After input, all companies with a route with such number are displayed. Click on a company to show all stops on their route.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/main_activity_input_route.png" alt="MainActivity Input Route" width="25%" height="25%">

#### Jointly-operated routes
These are same route operated by one of KMB/LWB & one of CTB/NWFB. The companies operating the route will be displayed as below. (Note that LWB routes are displayed as KMB since no separate API to differentiate.)

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/main_activity_input_route_joint.png" alt="MainActivity Input Jointly-operated Route" width="25%" height="25%">

#### Circular routes
These are routes that have only 1 terminus, directly returning back to its origin after reaching its outbound destination. Only outbound destination is shown.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/main_activity_input_route_circular.png" alt="MainActivity Input Circular Route" width="25%" height="25%">

#### No route found
Alternatively, if no such route is found in any company, an error message is displayed.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/main_activity_input_route_error.png" alt="MainActivity Input Route Error" width="25%" height="25%">

### 2. SelectStopActivity
The screen displays all normal operation bus stops on the route (Note: stops which are served only during diversion are not shown for KMB routes). Click on any stop to view the ETA.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/stop_activity.png" alt="StopActivity" width="25%" height="25%">

Note: for jointly-operated routes, the stop names of KMB are used by default.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/stop_activity_joint.png" alt="StopActivity Jointly-operated routes" width="25%" height="25%">

### 3. SelectTimeActivity
This screen displays the first ETA of the selected route, direction and stop. It displays in both time (UTC+8) and countdown in minutes.

Notes:
#### 3.1 Joint-operation
Both companies' API are called, and stops are matched by latitude and longitude not exceeding 2e-3 degree of each other. (Note: the outbound/inbound directions of cross harbour routes is opposite between KMB and CTB/NWFB but same for non-cross harbour routes.) The earliest ETA is shown on screen. The company TextView will only show the operator of that departure. The destination and stop name will be displayed according to the operator's names.

For example, below N619 (jointly-operated by CTB/KMB) departure at 02:37 is operated by CTB.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/time_activity.png" alt="TimeActivity" width="25%" height="25%">

#### 3.2 No ETA found
The ETA API returns departures within an hour. If no ETA is returned, below error message is displayed. It may be due to out of service hours of route or data error. Use the Refresh button to refresh the status.

<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/time_activity_no_eta.png" alt="TimeActivity No ETA" width="25%" height="25%">

## Future developments
1. Allow app to send notification of ETA of certain route at specified time.
