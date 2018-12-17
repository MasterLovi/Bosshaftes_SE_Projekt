# Bosshaftes SE Projekt  

## Installation  

### Downloads  

Github Desktop runterladen und konfigurieren  
- Git Repository �ber URL klonen: <https://github.com/MasterLovi/Bosshaftes_SE_Projekt.git>  
 
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

## Funktionalit�t:   
�BSP� � ein Projekt zur Ansicht, �bersicht und Planung von Routen mit Zwischenstopps auf St�dtebasis. Der Grundlegende Gedanke ist es, dem Anwender eine Software zur Verf�gung zu stellen, mit der Routen, die aus einzelnen, so genannten �Spots� bestehen, angesehen und geplant werden k�nnen. Diese Spots sind hierbei Orte in der Stadt, die von Touristen besucht werden k�nnen, wie Museen, Bars oder �hnlichem.  
Die Zielgruppe sind Menschen, die einen k�rzeren (maximal einen Tag) Aufenthalt in einer fremden Stadt zu absolvieren haben, der ihnen frei zur Verf�gung steht. Ein Beispiel daf�r w�ren gesch�ftliche Termine, f�r die man vormittags anreist, die aber tats�chlich erst gegen Abend beginnen oder die Verabredung mit einem Freund zum Brunch, bei dem man bereits am Vortag anreist. In beiden F�llen hat man entweder einen Nachmittag oder einen Abend, den man allein in einer unbekannten Umgebung verbringen muss.  
Viele Menschen w�rden die Zeit gerne kulturell nutzen, also beispielsweise die Wahrzeichen der Stadt kennenlernen, ein Museum besuchen oder die Altstadt besichtigen. Dabei hat man entweder die bekanntesten Touristenattraktionen im Sinn, oder aber Geheimtipps, die nicht von jeder Website empfohlen werden.  
F�r die Abende k�nnen attraktive Bars oder Clubs besucht werden oder gar eine �Kneipentour� absolviert werden � so lernt man leicht neue Leute kennen und f�hlt sich nicht allein oder verloren.  
In beiden F�llen ist es, vor allem wenn die zur Verf�gung stehende Zeit knapp bemessen ist, nicht immer einfach und oftmals ist den Leuten die M�he nicht wert, ein bis zwei Stunden oder einen halben Tag akribisch zu planen, um die Zeit bestm�glich zu nutzen.  
Daher bietet unsere Anwendung vorab die M�glichkeit, aus den zwei Kategorien �Kultur� oder �Party� auszuw�hlen, um dann direkt von uns oder von anderen Nutzern erstellte Routen angezeigt zu bekommen und die f�r einen passende auszuw�hlen. Der Fokus liegt hierbei darauf, dem Nutzer m�gliche Spots zu pr�sentieren und diese optimal an seine Anspr�che angepasst zu kombinieren und zu verbinden. Daher bieten wir Auswahlm�glichkeiten, um auf unterschiedliche Touren zu filtern. Routen k�nnen sowohl nach der Anzahl der Spots oder nach der zur Verf�gung stehenden Zeit gefiltert werden. Dabei bedingen sich die beiden Kriterien gegenseitig, es ist nicht m�glich nach Kombinationen zu suchen, die nicht existieren. Beide Optionen sind maximale Gr��en, dementsprechend werden alle Routen angezeigt, die kleiner oder gleich dem angegebenem Filterwert sind.  
Erg�nzt wird diese Filter- und Suchfunktion noch durch eine Beschr�nkung auf ein bestimmtes Nutzerranking. Dabei geht es um eine Sternewertung, die bei den Ergebnissen nicht unterschritten werden darf. Die Bewertungsfunktion besteht aus einem von mindestens einem bis maximal f�nf Sterne zu vergebendem Rating f�r Routen und Spots, wobei hier zus�tzlich ein Kommentar abgegeben werden muss.  
Der Nutzer ist dann w�hrend der Ansicht einer Route in der Lage, die von der Software optimale Routenf�hrung �ber andere Strecken anzupassen. Dazu kann er Zwischenstopps per Drag and Drop verschieben.  
Sucht der Nutzer allerdings nicht nach ganzen Routen, sondern vorerst nur nach Spots, werden beim initialen Laden der Karte schon Lokalit�ten im Umkreis angezeigt. Um den Nutzer nicht mit Ergebnissen zu �berfluten, werden nur Punkte in einem Umkreis von 100km um das momentane Zentrum der Karte angezeigt. Beim Scrollen �ber die Karte werden die Spots im ge�nderten Sichtbereich neu geladen, dies gilt auch f�r das Verwenden der Zoom-Funktion. Ab einem Ma�stab 1:250.000 werden aus Performancegr�nden keine Spots mehr geladen.  
Dem Nutzer stehen zwei M�glichkeiten zur Verf�gung, sich auf der Karte zu orten. Entweder er erlaubt der Anwendung, ihn per GPS zu tracken, oder er kann �ber eine eingebaute Suchfunktion nach seiner momentanen oder baldigen Position suchen. Diese Position bleibt sp�ter dauerhaft kenntlich, um f�r bessere Orientierung zu sorgen.  
Sowohl zu den Routen, als auch zu den Spots werden dem Nutzer verschiedene Daten �ber Pop-ups angezeigt, die erscheinen, wenn der Nutzer auf einen Punkt klickt. Dasselbe gilt f�r Routen, die nach vorangegangener Suche ausgew�hlt werden k�nnen. Sobald solch eine Auswahl geschehen ist, fokussiert die Anwendung auf den Spot oder bei Routen auf ein Sichtfenster, welches die gesamte Route abbilden kann. Dazu gibt es jeweils einen Namen, eine kurze Beschreibung in Textform und (mindestens) ein Bild. Name und Beschreibung f�r jede Route und jeden Spot sind hierbei obligatorisch.  
W�hrend allgemeine Informationen und Bewertungen von allen Nutzern angesehen werden k�nnen, steht die Abgabe von Kommentaren und Bewertungen nur angemeldeten Nutzern zu. Auch kann ein Nutzer lediglich die von ihm erstellten Routen, Kommentare und Bewertungen wieder l�schen oder ver�ndern. Daher existiert eine Nutzerverwaltung � jeder Besucher der Website hat die M�glichkeit einen Account zu erstellen. Dieser ist mit einer E-Mail-Adresse verbunden und verf�gt �ber einen �ffentlichen Nutzernamen.  
Das Hauptargument f�r die Anmeldung ist die hiermit zur Verf�gung stehende Funktion sich eigene Routen zu erstellen oder neue Spots hinzuzuf�gen. Eine bereits identifizierte Schwachstelle ist hierbei die M�glichkeit, die Anwendung mit Lokalit�ten zu �berfluten, die m�glicherweise gar nicht existieren. Zur L�sung dieses Problems gibt es derzeit ein Meldesystem, dass es angemeldeten Nutzer erm�glicht, jeweils einmalig einen Spot als fehlerhaft oder nicht existent zu melden. Nach drei Meldungen durch unterschiedliche Nutzer wird der dieser schlie�lich automatisch gel�scht.  
Die Anwendung ist alles in allem recht schlicht gehalten und auf die zwei Key-Features Routenplanung und Spot-�bersicht beschr�nkt. F�r mehr Individualit�t wurden diese durch eine Nutzerverwaltung erg�nzt. Damit soll dem Nutzer eine m�glichst �bersichtliche und effizient gestaltete Anwendung zur Verf�gung gestellt werden, mit der er seine Freizeit optimal ausgestalten kann.  


## Eingesetzte Technik:  
Das Team hat sich im Kickoff-Meeting eingehend mit der zur Umsetzung der Initial-Idee notwendigen Techniken auseinandergesetzt.  
Die Programmierarbeit erfolgte �ber die Integrierte Entwicklungsumgebung (IDE) Eclipse, da sie in der Lage ist, eine gro�e Auswahl von Funktionalit�t zu liefern und weiterhin integrierte Datenbank- und Serverfunktionalit�ten bietet, welche den Entwicklungs- und Testprozess der Applikation deutlich erleichterten. Au�erdem wurde Git zur Versionsverwaltung genutzt.  
Da es sich um eine Web-Anwendung handelt, mussten auf jeden Fall sowohl HTML als auch CSS zum Einsatz kommen, um den Aufbau und das Design des Frontends zu erstellen. Zus�tzlich wurde JavaScript f�r den Einbau von Funktionalit�t verwendet. Dabei werden einfache HTML Manipulationen, Anfragen an den Webserver und Funktionalit�t in JavaScript abgebildet. Des Weiteren werden auch API Aufrufe dadurch gesteuert.  
Das Backend, also die Datenbankverwaltung und Serverkommunikation sollten in Java umgesetzt werden, weil das komplette Team bereits (unterschiedlich stark ausgepr�gte) Erfahrung mit dieser Sprache hat.  
Daher wurde entschieden, die Applikation mit JSF (Java Server Faces), einem Java Web-Application-Framework, zu entwickeln, und dabei vor allem die Technologien JSP (Java Server Pages) und Java Servlets zu nutzen.  
Das Zusammenspiel all dieser Techniken erforderte einen gewissen Lernprozess der beteiligten Programmierer, da nicht alle Sprachen bereits im Vorfeld beherrscht wurden.  
Letzten Endes hat das Team die f�r die eigenen F�higkeiten in Zusammenspiel mit dem Anwendungsfall sinnvollste und einfachste Kombination aus Techniken verwendet, um das bestm�gliche Produkt erschaffen zu k�nnen.  

## Softwarearchitektur:  
Die gesamte Applikation �BSP� l�uft auf einem Tomcat v8.5 Webserver, der zum jetzigen Zeitpunkt lokal auf dem Ger�t des Users bzw. auf einem Ger�t in dessen Netzwerk laufen muss, um die Anwendung in diesem Netzwerk verf�gbar zu machen. In naher Zukunft w�re nat�rlich angedacht, den Server zentral oder in einer Cloud laufen zu lassen, um die Applikation f�r alle online erreichbar zu machen. Der User kommuniziert aber nicht nur mit dem Server und bedient somit die Applikation, sondern es werden auch clientseitige Aufrufe an die API von MapQuest gesendet, die ihm Routen und Adressen zur�ckgibt.  
Auf dem Server liegen die klassischen drei Schichten einer Web-Applikation: die Pr�sentationsschicht, die Applikationsschicht und die Datenbankschicht. Die Pr�sentationsschicht ist dabei aus statischem HTML, JSP (Java Server Pages) und einigen JavaScript-Bibliotheken aufgebaut.  
Die Applikationsschicht und somit das Backend der Anwendung basiert auf Java Servlets und Java Server Pages. Zur Kommunikation mit der Datenbankschicht hierbei wird der EntitiyManager der Java Persistence API verwendet, welcher sowohl das Object-Relationship-Mapping der Entit�ten �bernimmt, als auch die in der Persistence-Unit beschriebene Verbindung zur Datenbank aufbaut.  
Der EntityManager kommuniziert schlussendlich �ber JPQL und SQL mit einer Derby Datenbank, welche ebenfalls auf dem Server liegt und auf Anfrage Daten zur�ckgibt.  
Zus�tzlich ist auch der Aufbau der JPA-Klassen wichtig f�r die Softwarearchitektur. Dieser findet sich in der n�chsten Abbildung in Form eines UML-Klassendiagramms. Wie dort zu sehen ist, wurden zur Abbildung aller n�tigen Anforderungen insgesamt f�nf Entity-Klassen und eine Utility-Klasse erstellt. Die Utility-Klasse Time wurde ben�tigt, da es in den von uns durchsuchten Java-Bibliotheken keine geeignete Klasse gab, die eine Zeitdauer so abbildete, wie wir es f�r unseren Zweck ben�tigten.  
Auch schon vor Beginn der Programmierarbeit wurde ein erstes UML-Klassendiagramm angelegt, um die JPA-Klassen initial zu definieren. Jedoch haben sich vor allem die Assoziationen sowie auch der Umfang der Entit�ten �Location� und �Route� �ber den Verlauf des Projekts hinweg stark ver�ndert.  
Zus�tzlich zu den klassischen JPA-Annotationen wie @Entity, @Table, @Column, @Id oder auch den Assoziations-Annotationen @OneToMany oder @ManyToMany, die f�r die korrekte Abbildung von Klassen auf Datenbanktabellen und Hilfstabellen verantwortlich sind, wurden weitere Annotationen ben�tigt, um vor allem die Zusammenarbeit zwischen Pr�sentations- und Applikationsschicht zu erleichtern. Zum einen werden alle Prim�rschl�ssel durch die Annotation @GeneratedValue von JPA automatisch generiert. Au�erdem wurde die Annotation @Expose verwendet, um zu entscheiden, welche Attribute im JSON-Format an die Pr�sentationsschicht weitergegeben werden sollen. Auf der anderen Seite war es mit der Annotation @Transient m�glich, bestimmte Attribute, die nur f�r Berechnungen oder Umwandlungen ben�tigt wurden, nicht in der Datenbank abzuspeichern.  

## Qualit�tssicherung:  
Bei der Umsetzung eines Softwareprojektes im Unternehmensumfeld liegt ein besonderer Aspekt auf der Qualit�t der Software, weshalb im Folgenden die Qualit�tssicherung unseres Projektes besonders beleuchtet werden soll. 
Wir haben aus vorherigen Semestern bereits gelernt, dass Software-Qualit�t in Projekten nicht zu vernachl�ssigen ist. Um daher eine angemessene Qualit�t zu gew�hrleisten, musste sich auf bestimmte Eigenschaften geeinigt werden, die f�r das Erstellen und Inbetriebnehmen unserer Software von besonderer Bedeutung waren. Einige davon sind dabei f�r ein bestimmtes Software-Produkt besonders wichtig, andere wiederum vollst�ndig irrelevant. Manche Eigenschaften stehen au�erdem miteinander in negativer Wechselwirkung.[1] Aufgrund dieser Tatsache wurde im Laufe des Projektes ein Meeting angesetzt, um �ber die anzustrebenden Qualit�tsmerkmale zu entscheiden. Besonderer Wert sollte auf die Eigenschaften Zuverl�ssigkeit, Benutzerfreundlichkeit, Funktionalit�t und vor allem auch Wartbarkeit und Erweiterbarkeit gelegt werden.   
Der Begriff Funktionalit�t umfasst zudem konkrete Eigenschaften wie Korrektheit, Angemessenheit und auch Sicherheit.  
Erweiterbarkeit und Wartbarkeit beschreiben hierbei die Eigenschaften �nderbarkeit, Stabilit�t, Testbarkeit und auch Analysierbarkeit in momentanen, sowie in sp�teren Entwicklungsstadien neuer bzw. zus�tzlicher Funktionen. Softwaresysteme werden immer komplexer, immer neue Softwareentwickler m�ssen sich in die Projekte einarbeiten. Besonders im Hinblick auf die praxisnahe Anwendung sollte deshalb auch auf die Wartbarkeit geachtet werden. 
W�hrend Benutzerfreundlichkeit hingegen Merkmale wie Verst�ndlichkeit, Bedienbarkeit und Erlernbarkeit beinhaltet, umfasst die Zuverl�ssigkeit Eigenschaften wie Fehlertoleranz und Wiederherstellbarkeit und definiert somit auch die Wahrscheinlichkeit des fehlerfreien Funktionierens �ber eine bestimmte Zeitspanne bei einer bestimmten Betriebsweise[2].  
Im Hinblick auf die hier definierten Qualit�tsmerkmale wurde unsere Software vom Team nach jedem Sprint �berpr�ft. Am Ende des Programmierprozesses wurde noch einmal vollumfassend von jedem Team Mitglied untersucht, ob jede Eigenschaft realisiert und eingehalten wurde.  
Um ein prozessorientiertes Qualit�tsmanagement zu gew�hrleisten, entschied sich das Team weiterhin f�r die Anwendung von Testwerkzeugen. Da das manuelle Ausf�hren von Tests schnell zu einer monotonen und dadurch wiederum fehleranf�lligen Aufgabe werden kann, stand hier das Erstellen automatisierter Tests im Fokus. Das Team wollte das Testen der Software einfach, schnell und mehrmals anwendbar erstellen, was die Motivation zur Erstellung von mehreren JUnit Tests ausgef�hrt �ber eine Test Suite war. Dabei wurde f�r jede Klasse ein Testfall implementiert, um jede Instruktion innerhalb des Codes automatisch zu testen.  
Durch das bewusste Ausf�hren aller Methoden mit Parametern, welche zu Fehlern f�hren, konnte so gepr�ft werden, ob gewisse Fehlermeldungen zur�ckgegeben wurden und ob sich das Programm so verh�lt, wie es das Entwicklungsteam erwartet. Au�erdem wurde so auch �berpr�ft, ob alle Daten korrekt gespeichert werden und ob das zur�ckgegebene Datenformat zutreffend ist.   
Um am Ende einen �berblick zu erhalten, wie vollst�ndig die Tests waren, wurde die Code Coverage offengelegt. Das sollte einen �berblick �ber den Anteil der tats�chlich durchgef�hrten Tests zu den theoretisch m�glichen Tests bereitstellen. Ergebnis war hier eine Code Coverage von 99,6%. Dies gab dem Team eine gewisse Sicherheit, dass auch die Qualit�tsmerkmale Zuverl�ssigkeit und Funktionalit�t in unserem Rahmen erreicht wurden. 
�Testen ist der Prozess, ein Programm mit der Absicht auszuf�hren, Fehler zu finden[3]�, sagte 1979 schon Glenford Myers. Aus dieser Absicht heraus, entschied sich das Team nach den automatisierten Tests der Applikationsfunktionalit�t auch noch Alpha- und Beta-Tests durchzuf�hren. Durch sorgf�ltiges Testen und anschlie�endem Korrigieren von gefunden Fehlern sollte die Wahrscheinlichkeit gesenkt werden, dass das Programm sich nicht wunschgem�� verh�lt, oder dass der Nutzer durch seine Interaktionen schwerwiegende Fehler verursachen kann.  
Als erstes wurde ein Alpha-Test unternommen. Das bedeutet, dass die Entwickler ihr Programm selbst ausf�hrten und bewusst nach Fehlern suchten. Falls die Anwendung nicht fehlerfrei lief, wurden die Hindernisse behoben. Mit diesen Ma�nahmen konnte das Team sicherstellen, dass alle Funktionalit�ten und Anforderungen der Software so erf�llt wurden, wie am Anfang des Projektes bestimmt.  
Der n�chste Test bestand dann daraus, dass mehrere Teammitglieder das Programm testeten, ohne dabei Zugang zum System bzw. Zum Quellcode zu haben. Die Anwendung wurde nutzungsgetreu von den Personen ausgef�hrt, wobei alle m�glichen ausf�hrbaren Punkte des Programms getestet wurden. Gefundene Fehler wurden anschlie�end wiederum von den Programmierern gesucht und behoben.  
Als letzten Testfall wurde ein User-Acceptance-Test/Beta-Test der Anwendung mit zwei Au�enstehenden durchgef�hrt, wof�r ein Meeting mit Mitgliedern des Teams und zwei neutralen Personen aufgesetzt wurde. Den neutralen Anwendern wurde das Programm vorgestellt und sie sollten daraufhin die Applikation selbst benutzen und bewerten. Beide Personen erstellten eigene Routen, f�gten neue Orte zu der Karte hinzu und bewerteten und kommentierten ebenfalls einige Standard-Routen. Bei diesen Testf�llen traten keine weiteren Fehler auf. Allerdings konnte n�tzliches Feedback zur Benutzerfreundlichkeit und �bersichtlichkeit gesammelt werden. Bis zur Abgabe des Projektes war zu diesem Zeitpunkt nicht mehr ausreichend Zeit, um alle Punkte zum Thema Benutzerfreundlichkeit tiefgehend zu verarbeiten. Ein paar kleine designtechnische Details wie zum Beispiel ver�nderte Schriftgr��e und Schriftart, Platzierung des Logos und Platzierung der Standortmarkierung und Pop-Ups konnten jedoch umgesetzt werden.  
Dar�ber hinaus konnte das Team �berpr�fen, ob alle Funktionen, so wie am Anfang des Projektes definiert, umgesetzt werden konnten. 
