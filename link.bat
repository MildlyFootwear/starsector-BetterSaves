@echo off

@echo Linking %1\common to %CD%\common.
@echo Enter Y to confirm.


set /p "confirm="
 if %confirm% == Y (
	if exist %1\common ( 
		rename %1\common "commonbackup"
		@echo Renamed existing common folder to commonbackup.
	)
	mklink /j %1\common "%CD%\common"
) else (
@echo Link cancelled.)
pause