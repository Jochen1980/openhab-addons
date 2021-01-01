# joba Binding

## Notes
* JobaThing vom ThingType 'sample', siehe thing-types.xml
* JobaThingHandler-Klasse
* Backendparameter: String config1 (siehe jobaConfiguration)
* Channel 1: id = channel1, ChannelType sample-channel, ItemType String
* Channel 2: id = switch1, ChannelType jobaswitch, ItemType Switch (siehe thing-types.xml und jobaBindingConstants.java)
* Eventhandling in 

## User Interface
* OH3: neuer Weg, basic ui weiterhin moeglich
* Pages-Konzept: 
  * localhost:8080 - User Sicht fuer alle.
  * localhost:8080 - Eingeloggt ist man im Adminpanel.
  * Beispiel-UI in Demo - https://demo.openhab.org/#!/page/temperatures
* Python-Rules-Konzept: 
  * tbd
* Persistenz
  * Konfiguration in xyz.persist Dateien, etwa 
  * bei items kann die strategy via Attribut angegeben werden.
  * DB liegt etwa in /var/lib/openhab/peristence/rrd4j/
  * rrd4j Daten lassen sich am besten via Rest-API betrachten, http://IP:PORT/rest/persistence/rrd4j/ITEMNAME
  * etwa http://localhost:8080/rest/persistence/items/JobaDevSwitch (man muss eingeloggt sein)
  * InfluxDB ist beliebt und hier wird 


## Ways to go for CUS binding
* Call via Postman
* Integrate it here
* Enrich it to a use case
* commit the addon

## Comparison WebThings Approaches
* Developers sind Sven und Gregor


## Comparison HomeConnect Approaches
* Developers sind Stefan und Jonas
* Github Stefan (S): https://github.com/Jochen1980/org.openhab.binding.homeconnect
* Github Jonas (J): https://github.com/bruestel/org.openhab.binding.homeconnect/tree/2.5.x-next/bundles/org.openhab.binding.homeconnect
* Genereller Vergleich
    * J: hat einen geordneten Aufbau, Readme sehr gut. 
* thing-types.xml
    * J: ueblichsten Geraete sind mit drin, leider kein Cook-It
    * J: Client-Aufbau gefaellt mir sehr gut. 
    