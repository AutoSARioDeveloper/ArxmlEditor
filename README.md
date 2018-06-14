### ARXML Editor

##### Copyright
Copyright (C) 2015-2018, PopcornSAR AUTOSAR IDE Team 

##### License

This Source Code Form is subject to the terms of the Eclipse Public License, v. 2.0. If a copy of the EPL was not distributed with this file, You can obtain one at http://www.eclipse.org/legal/epl-2.0/.

##### ARXML Editor Overview
----------------
The ARXML Editor is implemented for helping to write Arxml files more easily. 
ARXML Editor is based on Eclipse XML Editor, and it basically has Eclipse XML Editor and these functions additionally: 
* Auto indenting, and inputting SHORT-NAME Tag.
* Auto searching for schema location
* Content Assist for writing path of referrable classes

##### Requirements
- Eclipse IDE for Java and DSL Developers (Oxygen recommended)
- ARXML Schemas

##### Cautions
1. This ARXML Editor does not have ARXML schemas so, you need  add ARXML schema files in the project/ARXML_Schema/
 - You can download ARXML schema at  https://www.autosar.org/
2. Auto lining and Auto adding SHORT-NAME Tag is not meaning that AUTOSAR class must have children class or SHORT-NAME tag.

##### Code Contributors
Junnoh Lee