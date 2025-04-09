# Influencer Explorer – UML Class Diagram

```mermaid
classDiagram

%% === MODEL ===
class Influencer {
  - String name
  - String platform
  - String category
  - int followerCount
  - String country
  - double adRate
  + getName()
  + getPlatform()
  + getCategory()
  + getFollowerCount()
  + getCountry()
  + getAdRate()
  + setName(name)
  + setPlatform(platform)
  + setCategory(category)
  + setFollowerCount(count)
  + setCountry(country)
  + setAdRate(rate)
}

class IRepository {
  <<interface>>
  + save(Object)
  + delete(Object)
  + findAll() List
}

class ISearchable {
  <<interface>>
  + searchByName(name) List~Influencer~
}

class IFilterable {
  <<interface>>
  + filterByPlatform(platform) List~Influencer~
  + filterByCategory(category) List~Influencer~
  + filterByFollowerRange(min, max) List~Influencer~
  + filterByCountry(country) List~Influencer~
}

class ISortable {
  <<interface>>
  + sortByName() List~Influencer~
  + sortByFollowers() List~Influencer~
  + sortByAdRate() List~Influencer~
}

class InfluencerRepository {
  - List~Influencer~ influencers
  + save(Influencer)
  + delete(Influencer)
  + findAll()
  + searchByName(name)
  + filterByPlatform(platform)
  + filterByCategory(category)
  + filterByFollowerRange(min, max)
  + filterByCountry(country)
  + sortByName()
  + sortByFollowers()
  + sortByAdRate()
  - validateInfluencer(Influencer)
}

class UserFavorites {
  - String userName
  - List~Influencer~ favorites
  + addItem(Influencer)
  + removeItem(Influencer)
  + getAllItems()
  + searchByName(name)
}

class SubscriptionManager {
  - List~String~ subscribedUsers
  + isSubscribed(userName)
  + subscribe(userName)
  + unsubscribe(userName)
}

class IExporter {
  <<interface>>
  + export(data, filePath) boolean
}

class AbstractExporter {
  <<abstract>>
  # prepareExport(data) String
  + export(data, filePath) boolean
  # formatData(data) String
}

class CSVExporter {
  # formatData(data) String
}

class JSONExporter {
  # formatData(data) String
}

%% === VIEW ===
class InfluencerListView {
  - boolean isVisible
  - List~Influencer~ influencers
  + render()
  + update(influencers)
  + displaySearchResults(results)
  + setVisible(visible)
}

class UserFavoritesView {
  - boolean isVisible
  - List~Influencer~ favorites
  + render()
  + update(favorites)
  + setVisible(visible)
}

class ExportView {
  - boolean isVisible
  + render()
  + showExportOptions()
  + showExportSuccess(path)
  + showExportError(error)
  + setVisible(visible)
}

class MainView {
  - InfluencerListView influencerListView
  - UserFavoritesView userFavoritesView
  - ExportView exportView
  + showInfluencerListView()
  + showUserFavoritesView()
  + showExportView()
  + render()
}

%% === CONTROLLER ===
class IController {
  <<interface>>
  + initialize()
  + handleRequest(action, params)
  + getControllerName()
}

class InfluencerController {
  - String controllerName
  - InfluencerRepository repository
  - ISearchable searcher
  - IFilterable filter
  - ISortable sorter
  - InfluencerListView view
  + initialize()
  + handleRequest(action, params)
  + getControllerName()
  + handleSearch(query)
  + handleFilter(criteria)
  + handleSort(sortBy, ascending)
  + loadAllInfluencers()
}

class UserFavoritesController {
  - String controllerName
  - UserFavorites model
  - ISearchable searcher
  - UserFavoritesView view
  + initialize()
  + handleRequest(action, params)
  + getControllerName()
  + addToFavorites(influencer)
  + removeFromFavorites(influencer)
  + loadAllFavorites()
  + searchFavorites(name)
}

class ExportController {
  - String controllerName
  - IExporter exporter
  - ExportView view
  + initialize()
  + handleRequest(action, params)
  + getControllerName()
  + handleExport(format, path, data)
  - getExporterForFormat(format)
}

class SubscriptionController {
  - String controllerName
  - SubscriptionManager model
  + initialize()
  + handleRequest(action, params)
  + getControllerName()
  + subscribe(userName)
  + unsubscribe(userName)
  + checkSubscription(userName)
}

class MainController {
  - InfluencerController influencerController
  - UserFavoritesController userFavoritesController
  - ExportController exportController
  - SubscriptionController subscriptionController
  - MainView mainView
  + initialize()
  + handleRequest(controllerName, action, params)
}

%% === RELATIONSHIPS ===
IRepository <|.. InfluencerRepository
ISearchable <|.. InfluencerRepository
IFilterable <|.. InfluencerRepository
ISortable <|.. InfluencerRepository

ISearchable <|.. UserFavorites
IExporter <|.. AbstractExporter
AbstractExporter <|-- CSVExporter
AbstractExporter <|-- JSONExporter

InfluencerRepository --> Influencer
UserFavorites --> Influencer

InfluencerController --> IRepository
InfluencerController --> ISearchable
InfluencerController --> IFilterable
InfluencerController --> ISortable
InfluencerController --> InfluencerListView

UserFavoritesController --> UserFavorites
UserFavoritesController --> ISearchable
UserFavoritesController --> UserFavoritesView

ExportController --> IExporter
ExportController --> ExportView

SubscriptionController --> SubscriptionManager

MainController --> InfluencerController
MainController --> UserFavoritesController
MainController --> ExportController
MainController --> SubscriptionController
MainController --> MainView

MainView --> InfluencerListView
MainView --> UserFavoritesView
MainView --> ExportView
