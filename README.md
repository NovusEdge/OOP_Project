# Finnish Municipality Information (FMI) App
Before beginning, there's one important thing to note: The implementation needed to make changes and deviate from the implementation plan, I hope that's ok :>.

## Class Structure

#### Data Classes
- `DataRetriever`: This class is responsible for retrieving the data from the APIs. It uses 3 different APIs to do so: `PXData`, `OpenWeatherMap` and `CurrentUVIndex`.
- **`MunicipalityData`**: This class is responsible for storing the data retrieved from the APIs.
- **`PopulationData`**: A subclass of **`MunicipalityData`** that stores the population data.
- **`WeatherData`**: A subclass of **`MunicipalityData`** that stores the weather data.

#### Fragment Classes
- **`PopulationDataFragment`**: This class is responsible for displaying the population data.
- **`WeatherDataFragment`**: This class is responsible for displaying the weather data.

#### Activity and Helper Classes
- **`MainActivity`**: This class is responsible for managing the fragments and the data retrieval process.
- **`CityInformationActivity`**: This class is responsible for displaying the information of a specific city.
- **`SearchHistoryView`**, **`SearchHistoryListAdapter`**, **`SearchHistoryViewHolder`**: These classes are responsible for displaying the search history.
- **`CityTabPagerAdapter`**: This class is responsible for managing the tabs in the **`CityInformationActivity`**.

Each of these come together to form the structure of the app. Please refer to inline comments in the source code for more information. The app lacks a _Quiz_ feature, but I hope the rest of the implementation is satisfactory. One more thing to note is that the search history feature does not seem to work on some devices but works on others. I'm not sure why this is the case, but I hope it's not a big issue.

## Group Members: 
- Aliasgar Khimani - 001581023 - mohammed.aliasgar.mohammedali.khimani@student.lut.fi
- Roope Reime - 001146565 - roope.reime@student.lut.fi

## Class diagram

Here's the new class diagram for the app:

![Class Diagram](class_diagram.png)