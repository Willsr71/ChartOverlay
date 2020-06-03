# Chartoverlay
[![Build Status](https://ci.noxal.net/job/Chartoverlay/badge/icon)](https://ci.noxal.net/job/ChartOverlay/)
![Java JDK v1.8](https://img.shields.io/badge/Java%20JDK-v1.8-blue.svg)

Description

# REST API
### Catalog
Information about all charts in [NOAA's catalog](https://www.charts.noaa.gov/RNCs/RNCs.shtml), updated weekly.  
Clicking on a link will take you to an example use case.

|Path|Description|
|---|---|
|[/catalog/list](https://charts.will.sr/catalog/list)|List all charts|
|[/catalog/\<chart\>](https://charts.will.sr/catalog/12347)|Get information about a chart|
|[/catalog/district/list](https://charts.will.sr/catalog/district/list)|List all available Coast Guard districts|
|[/catalog/district/\<district\>](https://charts.will.sr/catalog/district/1)|List all charts in a district|
|[/catalog/state/list](https://charts.will.sr/catalog/state/list)|List all available states|
|[/catalog/state/\<state\>](https://charts.will.sr/catalog/state/1)|List all charts in a state|
|[/catalog/region/list](https://charts.will.sr/catalog/region/list)|List all available regions|
|[/catalog/region/\<district\>](https://charts.will.sr/catalog/region/1)|List all charts in a region|

### Chart
Detailed information about a chart from the BSB version from NOAA.  
BSB Charts are available as zip files from the url [https://www.charts.noaa.gov/RNCs/<chart>.zip]()

|Path|Description|
|---|---|
|[/chart/\<chart\>](https://charts.will.sr/chart/12347)|Get information about a chart|

# BSB File format
Information about the BSB file format can be found at [libbsb's SourceForce page](http://libbsb.sourceforge.net/bsb_file_format.html) as well as detailed header information at the [OpenCPN Wiki](https://opencpn.org/wiki/dokuwiki/doku.php?id=opencpn:supplementary_software:chart_conversion_manual:bsb_kap_file_format).

Additional resources are NOAA's [Notice to Mariner's information page](https://ocsdata.ncd.noaa.gov/ntm/Info.aspx) and the [libbsb source](https://sourceforge.net/projects/libbsb/files/libbsb/libbsb-0.0.7/).