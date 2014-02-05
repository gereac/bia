;this file is part of installer for BIA
;Copyright (C)2009 GC Software Factory
;

# Defines
!define REGKEY "SOFTWARE\$(^Name)"
!define PRODUCT_RESCUEPATH "$TEMP\BIA"
!define COMPANY "GC Software Factory"
!define COMPANY_URL "http://www.gerea.com"
!define PRODUCT_NAME $(^Name)
!define PRODUCT_URL "http://www.gerea.com/products/bia"

!define SHORTCUT_NAME "$(ShortcutName).lnk"
!define SHORTCUT_TARGET "$INSTDIR\BiaApp.exe"
!define SHORTCUT_WORKDIR "$INSTDIR"
!define SHORTCUT_HOTKEY "ALT|CONTROL|C"
!define SHORTCUT_DESCRIPTION "$(ShortcutDescription)"

!ifndef BUILD_SOURCEPATH
  !define BUILD_SOURCEPATH "C:"
!endif

!ifndef NSIS_OUTPATH
  !define NSIS_OUTPATH "."
!endif

!ifndef EXE_VERSION
    !define EXE_VERSION 1.0.0.0
!endif

# MUI defines
  !define MUI_HEADERIMAGE
      !define MUI_HEADERIMAGE_BITMAP ".\images\header_icon.bmp" ; optional
  !define MUI_HEADERIMAGE_RIGHT
  !define MUI_BGCOLOR ABCDEF
  !define MUI_LICENSEPAGE_BGCOLOR ABCDEF
  !define MUI_LICENSEPAGE_CHECKBOX
  !define MUI_WELCOMEFINISHPAGE_BITMAP ".\images\welcome_icon.bmp"
  !define MUI_UNWELCOMEFINISHPAGE_BITMAP ".\images\welcome_u_icon.bmp"
  !define MUI_ICON "${NSISDIR}\Contrib\Graphics\Icons\orange-install-nsis.ico"
  !define MUI_UNICON "${NSISDIR}\Contrib\Graphics\Icons\orange-uninstall-nsis.ico"
  !define MUI_FINISHPAGE_NOAUTOCLOSE
  !define MUI_UNFINISHPAGE_NOAUTOCLOSE
  !define MUI_ABORTWARNING
  !define MUI_UNABORTWARNING
  !define MUI_LANGDLL_REGISTRY_ROOT HKLM
  !define MUI_LANGDLL_REGISTRY_KEY ${REGKEY}
  !define MUI_LANGDLL_REGISTRY_VALUENAME "Installer Language"


# Included files
    !include MUI2.nsh
    !include FileFunc.nsh
    !insertmacro GetRoot

# Reserved files
;!insertmacro MUI_RESERVEFILE_LANGDLL

# Installer pages
!insertmacro MUI_PAGE_WELCOME
!insertmacro MUI_PAGE_LICENSE ".\license\License.txt"
Page custom CustomPageFunction ;Custom page
!insertmacro MUI_PAGE_INSTFILES
!insertmacro MUI_PAGE_FINISH

!insertmacro MUI_UNPAGE_WELCOME
!insertmacro MUI_UNPAGE_CONFIRM
!insertmacro MUI_UNPAGE_INSTFILES

# Installer languages
!insertmacro MUI_LANGUAGE "English"
!insertmacro MUI_LANGUAGE "Romanian"

!include ".\languages\english.nsh"
!include ".\languages\romanian.nsh"

# Installer atributes

# Installer sections

Var CustomPage_Dialog
Var CustomPage_Label
Var CustomPage_Link
Var CustomPage_Freespace

# Installer attributes
; Set name using the normal interface (Name command)
Name $(Name)
Caption $(Caption)
OutFile "${NSIS_OUTPATH}\BiaSetup.exe"
InstallDir "$PROGRAMFILES\BIA"
InstallDirRegKey HKLM "${REGKEY}" "Install Location"
SetDateSave on
SetDatablockOptimize on
SilentInstall normal
CRCCheck on
XPStyle on
ShowInstDetails show
ShowUninstDetails show
BGFont Arial 48 BOLD
BGGradient ABCABC ABCFED 000000
InstallColors 63FFAE 7F0037
BrandingText "Copyright © 2009 ${COMPANY}"
; +++ installer font +++
/* Sets the installer font. Please remember that the font you choose must be present on the user's machine as well.
Use the /LANG switch if you wish to set a different font for each language.
There are two LangStrings named ^Font and ^FontSize which contain the font and font size for every language. */
SetFont Arial 9
; --- installer font ---
CheckBitmap "${NSISDIR}\Contrib\Graphics\Checks\modern.bmp"

# Version info
; Windows shows this information on the Version tab of the File properties.
VIProductVersion "${EXE_VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductName "${PRODUCT_NAME}"
VIAddVersionKey /LANG=${LANG_ENGLISH} ProductVersion "${EXE_VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyName "${COMPANY}"
VIAddVersionKey /LANG=${LANG_ENGLISH} CompanyWebsite "${COMPANY_URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileVersion "${EXE_VERSION}"
VIAddVersionKey /LANG=${LANG_ROMANIAN} FileVersion "${EXE_VERSION}"
VIAddVersionKey /LANG=${LANG_ENGLISH} FileDescription "Visit ${PRODUCT_URL}"
VIAddVersionKey /LANG=${LANG_ROMANIAN} FileDescription "Vizitati ${PRODUCT_URL}"
VIAddVersionKey /LANG=${LANG_ENGLISH} LegalCopyright "Copyright © 2009 GC Software Factory.  All rights reserved."
VIAddVersionKey /LANG=${LANG_ROMANIAN} LegalCopyright "Copyright © 2009 GC Software Factory.  Toate drepturile rezervate."

;Request application privileges for Windows Vista
RequestExecutionLevel user

# Installer sections
# Macro for creating a registry key
!define HKEY_CLASSES_ROOT 0x80000000
!define HKEY_CURRENT_USER 0x80000001
!define HKEY_LOCAL_MACHINE 0x80000002
!define HKEY_USERS 0x80000003
!define HKEY_PERFORMANCE_DATA 0x80000004
!define HKEY_CURRENT_CONFIG 0x80000005
!define HKEY_DYN_DATA 0x80000006
!define KEY_CREATE_SUB_KEY 0x0004
!macro CreateRegKey ROOT_KEY SUB_KEY
    Push $0
    Push $1
    System::Call /NOUNLOAD "Advapi32::RegCreateKeyExA(i, t, i, t, i, i, i, *i, i) i(${ROOT_KEY}, '${SUB_KEY}', 0, '', 0, ${KEY_CREATE_SUB_KEY}, 0, .r0, 0) .r1"
    StrCmp $1 0 +2
    SetErrors
    StrCmp $0 0 +2
    System::Call /NOUNLOAD "Advapi32::RegCloseKey(i) i(r0) .r1"
    System::Free 0
    Pop $1
    Pop $0
!macroend

Section -CopyFiles SEC0000
    SetOutPath $INSTDIR
    SetOverwrite on
    File /r "${BUILD_SOURCEPATH}\Builds\BIA\NSISBuild\*.*" 
    !insertmacro CreateRegKey ${HKEY_LOCAL_MACHINE} SOFTWARE\BIA
    WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\BIA Publisher COMPANY
    WriteRegStr HKEY_LOCAL_MACHINE SOFTWARE\BIA Version EXE_VERSION
    WriteRegStr HKLM "${REGKEY}\Components" Main 1
SectionEnd

Section -Shortcuts SEC0001
    SetShellVarContext all
    IfFileExists "$SMPROGRAMS\${COMPANY}\$(Name)\*.*" 0 +1
        CreateDirectory "$SMPROGRAMS\${COMPANY}\$(Name)"

    SetOutPath ${SHORTCUT_WORKDIR}
    CreateShortCut "$DESKTOP\${SHORTCUT_NAME}" "${SHORTCUT_TARGET}" "" "" ""\
                    SW_SHOWNORMAL "${SHORTCUT_HOTKEY}" "${SHORTCUT_DESCRIPTION}"
    CreateShortCut "$SMPROGRAMS\${COMPANY}\$(Name)\${SHORTCUT_NAME}" "${SHORTCUT_TARGET}" "" "" ""\
                    SW_SHOWNORMAL "${SHORTCUT_HOTKEY}" "${SHORTCUT_DESCRIPTION}"
    CreateShortcut "$SMPROGRAMS\${COMPANY}\$(Name)\$(Name) website.lnk" "${PRODUCT_URL}"
    CreateShortcut "$SMPROGRAMS\${COMPANY}\$(Name)\$(^UninstallLink).lnk" "$INSTDIR\uninstall.exe"
SectionEnd

Section -UnistallInformation SEC0002
    WriteRegStr HKLM "${REGKEY}" "Install Location" $INSTDIR
    SetOutPath $INSTDIR
    WriteUninstaller $INSTDIR\uninstall.exe
    /*Create a key with your product name under HKLM\Software\Microsoft\Windows\CurrentVersion\Uninstall
    to add entries to the "Add/Remove Programs" section in the Control Panel.*/
    ; required values
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayName "$(^Name)"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" UninstallString $INSTDIR\uninstall.exe
    ; optional values
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" InstallLocation $INSTDIR
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayIcon $INSTDIR\uninstall.exe
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" Publisher "${COMPANY}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" URLInfoAbout "${COMPANY_URL}"
    WriteRegStr HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" DisplayVersion "${EXE_VERSION}"
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoModify 1
    WriteRegDWORD HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)" NoRepair 1
SectionEnd

# Macro for selecting uninstaller sections
!macro SELECT_UNSECTION SECTION_NAME UNSECTION_ID
    Push $R0
    ReadRegStr $R0 HKLM "${REGKEY}\Components" "${SECTION_NAME}"
    StrCmp $R0 1 0 next${UNSECTION_ID}
    !insertmacro SelectSection "${UNSECTION_ID}"
    GoTo done${UNSECTION_ID}
next${UNSECTION_ID}:
    !insertmacro UnselectSection "${UNSECTION_ID}"
done${UNSECTION_ID}:
    Pop $R0
!macroend

# Uninstaller sections
Section /o -un.DeleteFiles UNSEC0000
    ;RMDir /r $INSTDIR
    DeleteRegValue HKLM "${REGKEY}\Components" Main
SectionEnd

Section -un.Shortcuts UNSEC0001
    SetShellVarContext all
    Delete "$DESKTOP\${SHORTCUT_NAME}"
    Delete "$SMPROGRAMS\${COMPANY}\$(Name)\${SHORTCUT_NAME}"
    Delete "$SMPROGRAMS\${COMPANY}\$(Name)\$(Name) website.lnk"
    Delete "$SMPROGRAMS\${COMPANY}\$(Name)\$(^UninstallLink).lnk"
    RmDir "$SMPROGRAMS\${COMPANY}\$(Name)"
    RmDir "$SMPROGRAMS\${COMPANY}"
SectionEnd

Section -un.UninstallInformation UNSEC0002
    DeleteRegKey HKLM "SOFTWARE\Microsoft\Windows\CurrentVersion\Uninstall\$(^Name)"
    Delete $INSTDIR\uninstall.exe
    DeleteRegValue HKLM "${REGKEY}" StartMenuGroup
    DeleteRegValue HKLM "${REGKEY}" "Install Location"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}\Components"
    DeleteRegKey /IfEmpty HKLM "${REGKEY}"
    DeleteRegValue HKEY_LOCAL_MACHINE SOFTWARE\BIA Version
    DeleteRegValue HKEY_LOCAL_MACHINE SOFTWARE\BIA Publisher
    DeleteRegKey /IfEmpty HKEY_LOCAL_MACHINE SOFTWARE\BIA
SectionEnd

/**
    Functions
**/

# Installer functions
Function .onGUIEnd
    BGImage::Destroy
FunctionEnd

Function .onInit
    LogSet on
    InitPluginsDir
    !define MUI_LANGDLL_ALLLANGUAGES
    !insertmacro MUI_LANGDLL_DISPLAY
FunctionEnd

# Uninstaller functions
Function un.onInit
    /* For the uninstaller, insert the MUI_UNGETLANGUAGE macro 
    in un.onInit to get the stored language preference */
    ;!insertmacro MUI_UNGETLANGUAGE
    ReadRegStr $INSTDIR HKLM "${REGKEY}" "Install Location"
    !insertmacro SELECT_UNSECTION DeleteFiles ${UNSEC0000}
FunctionEnd

Function CustomPageFunction
    !insertmacro MUI_HEADER_TEXT $(CUSTOM_PAGE_TITLE) $(CUSTOM_PAGE_SUBTITLE)
    
    nsDialogs::Create /NOUNLOAD 1018
    Pop $CustomPage_Dialog

    ${If} $CustomPage_Dialog == error
        Abort
    ${EndIf}
    
    ${NSD_CreateLabel} 0 0 100% 10u $(CUSTOM_PAGE_DESCRIPTION)
    Pop $CustomPage_Label

    ${NSD_CreateLink} 0 13u 100% 10u $(CUSTOM_PAGE_LINK)
    Pop $CustomPage_Link
    
    nsDialogs::CreateControl /NOUNLOAD STATIC ${WS_VISIBLE}|${WS_CHILD}|${WS_CLIPSIBLINGS} 0 0 23u 100% 10u ""
    Pop $CustomPage_Freespace

    Call UpdateFreeSpace
    
    nsDialogs::Show

FunctionEnd

Function UpdateFreeSpace

    ${GetRoot} $INSTDIR $0
    StrCpy $1 " bytes"

    System::Call kernel32::GetDiskFreeSpaceEx(tr0,*l,*l,*l.r0)

    ${If} $0 > 1024
    ${OrIf} $0 < 0
        System::Int64Op $0 / 1024
        Pop $0
        StrCpy $1 "Kb"
        ${If} $0 > 1024
        ${OrIf} $0 < 0
            System::Int64Op $0 / 1024
            Pop $0
            StrCpy $1 "Mb"
            ${If} $0 > 1024
            ${OrIf} $0 < 0
                System::Int64Op $0 / 1024
                Pop $0
                StrCpy $1 "Gb"
            ${EndIf}
        ${EndIf}
    ${EndIf}

    SendMessage $CustomPage_Freespace ${WM_SETTEXT} 0 "STR:$(CUSTOM_PAGE_FREESPACE): $0$1"

FunctionEnd