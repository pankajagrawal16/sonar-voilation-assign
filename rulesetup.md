## How to Set Up custom rule in sonar:

- open the sonar-pmd-plugin-1.1.jar found at (Sonar\sonar-3.4.1\extensions\plugins) after unzipping the jar using Sonar Installer.
- Copy paste the .class file in the  pmd-4.3.jar inside sonar-pmd-plugin-1.1.jar\META-INF\lib\ (please maintain the project hierarchy while copy pasting the .class file).
- Post an entry in the sonar-pmd-plugin-1.1.jar\META-INF\lib\pmd-4.3.jar\rulesets\naming.xml
- Create a new html file containing the rule description and message (with same name as entered in the naming.xml) in the sonar-pmd-plugin-1.1.jar\org\sonar\l10n\pmd\rules\pmd\
- Post an entry about the description and rule name in sonar-pmd-plugin-1.1.jar\org\sonar\l10n\pmd.properties (Do not delete the special character at the end of line).
- Post an entry in the sonar-pmd-plugin-1.1.jar\org\sonar\plugins\pmd\rules.xml. This entry maps to the one made in  sonar-pmd-plugin-1.1.jar\META-INF\lib\pmd-4.3.jar\rulesets\naming.xml in step 2. Kindly keep the two in sync.

- Restart the Sonar server and activate the newly created rules.

