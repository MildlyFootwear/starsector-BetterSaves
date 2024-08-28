@echo off

SET CurrentDirectory=%~dp0
for %%B in (%CurrentDirectory%.) do set parent=%%~dpB
for %%B in (%parent%.) do set grandparent=%%~dpB

@echo Linking %1\common to %grandparent%saves\common.
@echo Enter Y to confirm.
set /p "confirm="
if %confirm% == Y (
	if exist %1\common ( 
		rename %1\common "commonbackup"
		@echo Renamed existing common folder to commonbackup.
	)
	mklink /j "%1\common" "%grandparent%saves\common"
) else (
@echo Link cancelled.)
pause