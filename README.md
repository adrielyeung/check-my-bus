# check-my-bus
An Android app to check Hong Kong bus routes and estimated time of arrival (ETA) at each stop, using data from [Kowloon Motor Bus Company (1933) Limited (KMB) (incorporating Long Win Bus Company Limited (LWB))](https://data.gov.hk/en-data/dataset/hk-td-tis_21-etakmb), [Citybus Limited (CTB)](https://data.gov.hk/en-data/dataset/ctb-eta-transport-realtime-eta) and [New World First Bus Services Limited (NWFB)](https://data.gov.hk/en-data/dataset/nwfb-eta-transport-realtime-eta) under the [OpenAPI initiative of the Hong Kong Government](https://data.gov.hk/en/).

## How to use
1. Clone the repository into a local directory.
2. Open the project directory in Android Studio.

## APIs called


## Description
The app consists of 3 Activities, namely:

1. MainActivity
2. SelectStopActivity
3. SelectTimeActivity

### 1. MainActivity
<img src="https://github.com/adrielyeung/check-my-bus/blob/main/images/main_activity_init.png" alt="MainActivity Initial Screen" width="25%" height="25%">

#### Normal routes
The initial screen, allowing users to input route number in EditText. After input, all companies with a route with such number are displayed. Click on a company to select its route.

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
### 3. SelectTimeActivity

## Future developments
1. Allow app to send notification of ETA of certain route at specified time.
