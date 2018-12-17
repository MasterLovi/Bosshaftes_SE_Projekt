# Bosshaftes SE Projekt  

## Installation  

### Downloads  

Github Desktop runterladen und konfigurieren  
- Git Repository über URL klonen: <https://github.com/MasterLovi/Bosshaftes_SE_Projekt.git>  
 
Git Tutorial  
- Einsteiger mit GitHub Desktop: [Git and Github for Beginners](https://www.youtube.com/playlist?list=PL4-IK0AVhVjM-Qy-wXwjB2Y23_ofXYRie) (beide Videos)   
- Einsteiger: <https://rogerdudler.github.io/git-guide/index.de.html>  
- Bash: <https://git-scm.com/book/de/v1/Git-Grundlagen-Ein-Git-Repository-anlegen>  
 
 
Eclipse und Java and Tomcat and Derby  
- Download Eclipse Photon: <https://www.eclipse.org/photon/>  
- Download Java SDK 8: <https://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html>  
- Download Tomcat v8.5 Core: <https://tomcat.apache.org/download-80.cgi>  
- Download Derby DB Binaries: <https://db.apache.org/derby/releases/release-10.14.2.0.cgi>  


### Environment Variables  

Set Environment Variables  
  
- Go to System Properties --> Advanced --> Environment Variables  
- Add the following Variables to "System Variables"  
 
#### Java:  
 
JAVA_HOME  
- Path to your Java SDK Folder  
- Exmaple: C:\Program Files\Java\jdk1.8.0_181   
 
#### Derby:  

DERBY_HOME  
- Path to your Derby Binaries  
- Example: C:\Program Files (x86)\DerbyDB\db-derby-10.14.2.0-bin  
 
#### Tomcat  

CATALINA_BASE  
- Path to your tomcat folder  
- Ecample: C:\Program Files\Apache Software Foundation\Tomcat 8.0\apache-tomcat-8.5.32  

CATALINA_HOME  
- Path to your tomcat folder  
- Example: C:\Program Files\Apache Software Foundation\Tomcat 8.0\apache-tomcat-8.5.32  
 
 
Add the following paths to System Variable "PATH":  
 
Your Java Path   
- Example: C:\Program Files (x86)\Common Files\Oracle\Java\javapath  

Your Program Data Oracle Java Path  
- Example: C:\ProgramData\Oracle\Java\javapath  

Your Tomcat Servlet API  
- Example: C:\Program Files\Apache Software Foundation\Tomcat 8.0\apache-tomcat-8.5.32\lib\servlet-api.jar  

Your Java Binaries  
- Example: C:\Program Files\Java\jdk1.8.0_181\bin  

Your Derby Binaries  
- Example: C:\Program Files (x86)\DerbyDB\db-derby-10.14.2.0-bin\bin  

## Funktionalität:   
„BSP“ – ein Projekt zur Ansicht, Übersicht und Planung von Routen mit Zwischenstopps auf Städtebasis. Der Grundlegende Gedanke ist es, dem Anwender eine Software zur Verfügung zu stellen, mit der Routen, die aus einzelnen, so genannten „Spots“ bestehen, angesehen und geplant werden können. Diese Spots sind hierbei Orte in der Stadt, die von Touristen besucht werden können, wie Museen, Bars oder ähnlichem.  
Die Zielgruppe sind Menschen, die einen kürzeren (maximal einen Tag) Aufenthalt in einer fremden Stadt zu absolvieren haben, der ihnen frei zur Verfügung steht. Ein Beispiel dafür wären geschäftliche Termine, für die man vormittags anreist, die aber tatsächlich erst gegen Abend beginnen oder die Verabredung mit einem Freund zum Brunch, bei dem man bereits am Vortag anreist. In beiden Fällen hat man entweder einen Nachmittag oder einen Abend, den man allein in einer unbekannten Umgebung verbringen muss.  
Viele Menschen würden die Zeit gerne kulturell nutzen, also beispielsweise die Wahrzeichen der Stadt kennenlernen, ein Museum besuchen oder die Altstadt besichtigen. Dabei hat man entweder die bekanntesten Touristenattraktionen im Sinn, oder aber Geheimtipps, die nicht von jeder Website empfohlen werden.  
Für die Abende können attraktive Bars oder Clubs besucht werden oder gar eine „Kneipentour“ absolviert werden – so lernt man leicht neue Leute kennen und fühlt sich nicht allein oder verloren.  
In beiden Fällen ist es, vor allem wenn die zur Verfügung stehende Zeit knapp bemessen ist, nicht immer einfach und oftmals ist den Leuten die Mühe nicht wert, ein bis zwei Stunden oder einen halben Tag akribisch zu planen, um die Zeit bestmöglich zu nutzen.  
Daher bietet unsere Anwendung vorab die Möglichkeit, aus den zwei Kategorien „Kultur“ oder „Party“ auszuwählen, um dann direkt von uns oder von anderen Nutzern erstellte Routen angezeigt zu bekommen und die für einen passende auszuwählen. Der Fokus liegt hierbei darauf, dem Nutzer mögliche Spots zu präsentieren und diese optimal an seine Ansprüche angepasst zu kombinieren und zu verbinden. Daher bieten wir Auswahlmöglichkeiten, um auf unterschiedliche Touren zu filtern. Routen können sowohl nach der Anzahl der Spots oder nach der zur Verfügung stehenden Zeit gefiltert werden. Dabei bedingen sich die beiden Kriterien gegenseitig, es ist nicht möglich nach Kombinationen zu suchen, die nicht existieren. Beide Optionen sind maximale Größen, dementsprechend werden alle Routen angezeigt, die kleiner oder gleich dem angegebenem Filterwert sind.  
Ergänzt wird diese Filter- und Suchfunktion noch durch eine Beschränkung auf ein bestimmtes Nutzerranking. Dabei geht es um eine Sternewertung, die bei den Ergebnissen nicht unterschritten werden darf. Die Bewertungsfunktion besteht aus einem von mindestens einem bis maximal fünf Sterne zu vergebendem Rating für Routen und Spots, wobei hier zusätzlich ein Kommentar abgegeben werden muss.  
Der Nutzer ist dann während der Ansicht einer Route in der Lage, die von der Software optimale Routenführung über andere Strecken anzupassen. Dazu kann er Zwischenstopps per Drag and Drop verschieben.  
Sucht der Nutzer allerdings nicht nach ganzen Routen, sondern vorerst nur nach Spots, werden beim initialen Laden der Karte schon Lokalitäten im Umkreis angezeigt. Um den Nutzer nicht mit Ergebnissen zu überfluten, werden nur Punkte in einem Umkreis von 100km um das momentane Zentrum der Karte angezeigt. Beim Scrollen über die Karte werden die Spots im geänderten Sichtbereich neu geladen, dies gilt auch für das Verwenden der Zoom-Funktion. Ab einem Maßstab 1:250.000 werden aus Performancegründen keine Spots mehr geladen.  
Dem Nutzer stehen zwei Möglichkeiten zur Verfügung, sich auf der Karte zu orten. Entweder er erlaubt der Anwendung, ihn per GPS zu tracken, oder er kann über eine eingebaute Suchfunktion nach seiner momentanen oder baldigen Position suchen. Diese Position bleibt später dauerhaft kenntlich, um für bessere Orientierung zu sorgen.  
Sowohl zu den Routen, als auch zu den Spots werden dem Nutzer verschiedene Daten über Pop-ups angezeigt, die erscheinen, wenn der Nutzer auf einen Punkt klickt. Dasselbe gilt für Routen, die nach vorangegangener Suche ausgewählt werden können. Sobald solch eine Auswahl geschehen ist, fokussiert die Anwendung auf den Spot oder bei Routen auf ein Sichtfenster, welches die gesamte Route abbilden kann. Dazu gibt es jeweils einen Namen, eine kurze Beschreibung in Textform und (mindestens) ein Bild. Name und Beschreibung für jede Route und jeden Spot sind hierbei obligatorisch.  
Während allgemeine Informationen und Bewertungen von allen Nutzern angesehen werden können, steht die Abgabe von Kommentaren und Bewertungen nur angemeldeten Nutzern zu. Auch kann ein Nutzer lediglich die von ihm erstellten Routen, Kommentare und Bewertungen wieder löschen oder verändern. Daher existiert eine Nutzerverwaltung – jeder Besucher der Website hat die Möglichkeit einen Account zu erstellen. Dieser ist mit einer E-Mail-Adresse verbunden und verfügt über einen öffentlichen Nutzernamen.  
Das Hauptargument für die Anmeldung ist die hiermit zur Verfügung stehende Funktion sich eigene Routen zu erstellen oder neue Spots hinzuzufügen. Eine bereits identifizierte Schwachstelle ist hierbei die Möglichkeit, die Anwendung mit Lokalitäten zu überfluten, die möglicherweise gar nicht existieren. Zur Lösung dieses Problems gibt es derzeit ein Meldesystem, dass es angemeldeten Nutzer ermöglicht, jeweils einmalig einen Spot als fehlerhaft oder nicht existent zu melden. Nach drei Meldungen durch unterschiedliche Nutzer wird der dieser schließlich automatisch gelöscht.  
Die Anwendung ist alles in allem recht schlicht gehalten und auf die zwei Key-Features Routenplanung und Spot-Übersicht beschränkt. Für mehr Individualität wurden diese durch eine Nutzerverwaltung ergänzt. Damit soll dem Nutzer eine möglichst übersichtliche und effizient gestaltete Anwendung zur Verfügung gestellt werden, mit der er seine Freizeit optimal ausgestalten kann.  


## Eingesetzte Technik:  
Das Team hat sich im Kickoff-Meeting eingehend mit der zur Umsetzung der Initial-Idee notwendigen Techniken auseinandergesetzt.  
Die Programmierarbeit erfolgte über die Integrierte Entwicklungsumgebung (IDE) Eclipse, da sie in der Lage ist, eine große Auswahl von Funktionalität zu liefern und weiterhin integrierte Datenbank- und Serverfunktionalitäten bietet, welche den Entwicklungs- und Testprozess der Applikation deutlich erleichterten. Außerdem wurde Git zur Versionsverwaltung genutzt.  
Da es sich um eine Web-Anwendung handelt, mussten auf jeden Fall sowohl HTML als auch CSS zum Einsatz kommen, um den Aufbau und das Design des Frontends zu erstellen. Zusätzlich wurde JavaScript für den Einbau von Funktionalität verwendet. Dabei werden einfache HTML Manipulationen, Anfragen an den Webserver und Funktionalität in JavaScript abgebildet. Des Weiteren werden auch API Aufrufe dadurch gesteuert.  
Das Backend, also die Datenbankverwaltung und Serverkommunikation sollten in Java umgesetzt werden, weil das komplette Team bereits (unterschiedlich stark ausgeprägte) Erfahrung mit dieser Sprache hat.  
Daher wurde entschieden, die Applikation mit JSF (Java Server Faces), einem Java Web-Application-Framework, zu entwickeln, und dabei vor allem die Technologien JSP (Java Server Pages) und Java Servlets zu nutzen.  
Das Zusammenspiel all dieser Techniken erforderte einen gewissen Lernprozess der beteiligten Programmierer, da nicht alle Sprachen bereits im Vorfeld beherrscht wurden.  
Letzten Endes hat das Team die für die eigenen Fähigkeiten in Zusammenspiel mit dem Anwendungsfall sinnvollste und einfachste Kombination aus Techniken verwendet, um das bestmögliche Produkt erschaffen zu können.  

## Softwarearchitektur:  
Die gesamte Applikation “BSP” läuft auf einem Tomcat v8.5 Webserver, der zum jetzigen Zeitpunkt lokal auf dem Gerät des Users bzw. auf einem Gerät in dessen Netzwerk laufen muss, um die Anwendung in diesem Netzwerk verfügbar zu machen. In naher Zukunft wäre natürlich angedacht, den Server zentral oder in einer Cloud laufen zu lassen, um die Applikation für alle online erreichbar zu machen. Der User kommuniziert aber nicht nur mit dem Server und bedient somit die Applikation, sondern es werden auch clientseitige Aufrufe an die API von MapQuest gesendet, die ihm Routen und Adressen zurückgibt.  
Auf dem Server liegen die klassischen drei Schichten einer Web-Applikation: die Präsentationsschicht, die Applikationsschicht und die Datenbankschicht. Die Präsentationsschicht ist dabei aus statischem HTML, JSP (Java Server Pages) und einigen JavaScript-Bibliotheken aufgebaut.  
Die Applikationsschicht und somit das Backend der Anwendung basiert auf Java Servlets und Java Server Pages. Zur Kommunikation mit der Datenbankschicht hierbei wird der EntitiyManager der Java Persistence API verwendet, welcher sowohl das Object-Relationship-Mapping der Entitäten übernimmt, als auch die in der Persistence-Unit beschriebene Verbindung zur Datenbank aufbaut.  
Der EntityManager kommuniziert schlussendlich über JPQL und SQL mit einer Derby Datenbank, welche ebenfalls auf dem Server liegt und auf Anfrage Daten zurückgibt.  
Zusätzlich ist auch der Aufbau der JPA-Klassen wichtig für die Softwarearchitektur. Dieser findet sich in der nächsten Abbildung in Form eines UML-Klassendiagramms. Wie dort zu sehen ist, wurden zur Abbildung aller nötigen Anforderungen insgesamt fünf Entity-Klassen und eine Utility-Klasse erstellt. Die Utility-Klasse Time wurde benötigt, da es in den von uns durchsuchten Java-Bibliotheken keine geeignete Klasse gab, die eine Zeitdauer so abbildete, wie wir es für unseren Zweck benötigten.  
Auch schon vor Beginn der Programmierarbeit wurde ein erstes UML-Klassendiagramm angelegt, um die JPA-Klassen initial zu definieren. Jedoch haben sich vor allem die Assoziationen sowie auch der Umfang der Entitäten „Location“ und „Route“ über den Verlauf des Projekts hinweg stark verändert.  
Zusätzlich zu den klassischen JPA-Annotationen wie @Entity, @Table, @Column, @Id oder auch den Assoziations-Annotationen @OneToMany oder @ManyToMany, die für die korrekte Abbildung von Klassen auf Datenbanktabellen und Hilfstabellen verantwortlich sind, wurden weitere Annotationen benötigt, um vor allem die Zusammenarbeit zwischen Präsentations- und Applikationsschicht zu erleichtern. Zum einen werden alle Primärschlüssel durch die Annotation @GeneratedValue von JPA automatisch generiert. Außerdem wurde die Annotation @Expose verwendet, um zu entscheiden, welche Attribute im JSON-Format an die Präsentationsschicht weitergegeben werden sollen. Auf der anderen Seite war es mit der Annotation @Transient möglich, bestimmte Attribute, die nur für Berechnungen oder Umwandlungen benötigt wurden, nicht in der Datenbank abzuspeichern.  

## Qualitätssicherung:  
Bei der Umsetzung eines Softwareprojektes im Unternehmensumfeld liegt ein besonderer Aspekt auf der Qualität der Software, weshalb im Folgenden die Qualitätssicherung unseres Projektes besonders beleuchtet werden soll. 
Wir haben aus vorherigen Semestern bereits gelernt, dass Software-Qualität in Projekten nicht zu vernachlässigen ist. Um daher eine angemessene Qualität zu gewährleisten, musste sich auf bestimmte Eigenschaften geeinigt werden, die für das Erstellen und Inbetriebnehmen unserer Software von besonderer Bedeutung waren. Einige davon sind dabei für ein bestimmtes Software-Produkt besonders wichtig, andere wiederum vollständig irrelevant. Manche Eigenschaften stehen außerdem miteinander in negativer Wechselwirkung.[1] Aufgrund dieser Tatsache wurde im Laufe des Projektes ein Meeting angesetzt, um über die anzustrebenden Qualitätsmerkmale zu entscheiden. Besonderer Wert sollte auf die Eigenschaften Zuverlässigkeit, Benutzerfreundlichkeit, Funktionalität und vor allem auch Wartbarkeit und Erweiterbarkeit gelegt werden.   
Der Begriff Funktionalität umfasst zudem konkrete Eigenschaften wie Korrektheit, Angemessenheit und auch Sicherheit.  
Erweiterbarkeit und Wartbarkeit beschreiben hierbei die Eigenschaften Änderbarkeit, Stabilität, Testbarkeit und auch Analysierbarkeit in momentanen, sowie in späteren Entwicklungsstadien neuer bzw. zusätzlicher Funktionen. Softwaresysteme werden immer komplexer, immer neue Softwareentwickler müssen sich in die Projekte einarbeiten. Besonders im Hinblick auf die praxisnahe Anwendung sollte deshalb auch auf die Wartbarkeit geachtet werden. 
Während Benutzerfreundlichkeit hingegen Merkmale wie Verständlichkeit, Bedienbarkeit und Erlernbarkeit beinhaltet, umfasst die Zuverlässigkeit Eigenschaften wie Fehlertoleranz und Wiederherstellbarkeit und definiert somit auch die Wahrscheinlichkeit des fehlerfreien Funktionierens über eine bestimmte Zeitspanne bei einer bestimmten Betriebsweise[2].  
Im Hinblick auf die hier definierten Qualitätsmerkmale wurde unsere Software vom Team nach jedem Sprint überprüft. Am Ende des Programmierprozesses wurde noch einmal vollumfassend von jedem Team Mitglied untersucht, ob jede Eigenschaft realisiert und eingehalten wurde.  
Um ein prozessorientiertes Qualitätsmanagement zu gewährleisten, entschied sich das Team weiterhin für die Anwendung von Testwerkzeugen. Da das manuelle Ausführen von Tests schnell zu einer monotonen und dadurch wiederum fehleranfälligen Aufgabe werden kann, stand hier das Erstellen automatisierter Tests im Fokus. Das Team wollte das Testen der Software einfach, schnell und mehrmals anwendbar erstellen, was die Motivation zur Erstellung von mehreren JUnit Tests ausgeführt über eine Test Suite war. Dabei wurde für jede Klasse ein Testfall implementiert, um jede Instruktion innerhalb des Codes automatisch zu testen.  
Durch das bewusste Ausführen aller Methoden mit Parametern, welche zu Fehlern führen, konnte so geprüft werden, ob gewisse Fehlermeldungen zurückgegeben wurden und ob sich das Programm so verhält, wie es das Entwicklungsteam erwartet. Außerdem wurde so auch überprüft, ob alle Daten korrekt gespeichert werden und ob das zurückgegebene Datenformat zutreffend ist.   
Um am Ende einen Überblick zu erhalten, wie vollständig die Tests waren, wurde die Code Coverage offengelegt. Das sollte einen Überblick über den Anteil der tatsächlich durchgeführten Tests zu den theoretisch möglichen Tests bereitstellen. Ergebnis war hier eine Code Coverage von 99,6%. Dies gab dem Team eine gewisse Sicherheit, dass auch die Qualitätsmerkmale Zuverlässigkeit und Funktionalität in unserem Rahmen erreicht wurden. 
„Testen ist der Prozess, ein Programm mit der Absicht auszuführen, Fehler zu finden[3]“, sagte 1979 schon Glenford Myers. Aus dieser Absicht heraus, entschied sich das Team nach den automatisierten Tests der Applikationsfunktionalität auch noch Alpha- und Beta-Tests durchzuführen. Durch sorgfältiges Testen und anschließendem Korrigieren von gefunden Fehlern sollte die Wahrscheinlichkeit gesenkt werden, dass das Programm sich nicht wunschgemäß verhält, oder dass der Nutzer durch seine Interaktionen schwerwiegende Fehler verursachen kann.  
Als erstes wurde ein Alpha-Test unternommen. Das bedeutet, dass die Entwickler ihr Programm selbst ausführten und bewusst nach Fehlern suchten. Falls die Anwendung nicht fehlerfrei lief, wurden die Hindernisse behoben. Mit diesen Maßnahmen konnte das Team sicherstellen, dass alle Funktionalitäten und Anforderungen der Software so erfüllt wurden, wie am Anfang des Projektes bestimmt.  
Der nächste Test bestand dann daraus, dass mehrere Teammitglieder das Programm testeten, ohne dabei Zugang zum System bzw. Zum Quellcode zu haben. Die Anwendung wurde nutzungsgetreu von den Personen ausgeführt, wobei alle möglichen ausführbaren Punkte des Programms getestet wurden. Gefundene Fehler wurden anschließend wiederum von den Programmierern gesucht und behoben.  
Als letzten Testfall wurde ein User-Acceptance-Test/Beta-Test der Anwendung mit zwei Außenstehenden durchgeführt, wofür ein Meeting mit Mitgliedern des Teams und zwei neutralen Personen aufgesetzt wurde. Den neutralen Anwendern wurde das Programm vorgestellt und sie sollten daraufhin die Applikation selbst benutzen und bewerten. Beide Personen erstellten eigene Routen, fügten neue Orte zu der Karte hinzu und bewerteten und kommentierten ebenfalls einige Standard-Routen. Bei diesen Testfällen traten keine weiteren Fehler auf. Allerdings konnte nützliches Feedback zur Benutzerfreundlichkeit und Übersichtlichkeit gesammelt werden. Bis zur Abgabe des Projektes war zu diesem Zeitpunkt nicht mehr ausreichend Zeit, um alle Punkte zum Thema Benutzerfreundlichkeit tiefgehend zu verarbeiten. Ein paar kleine designtechnische Details wie zum Beispiel veränderte Schriftgröße und Schriftart, Platzierung des Logos und Platzierung der Standortmarkierung und Pop-Ups konnten jedoch umgesetzt werden.  
Darüber hinaus konnte das Team überprüfen, ob alle Funktionen, so wie am Anfang des Projektes definiert, umgesetzt werden konnten. 
