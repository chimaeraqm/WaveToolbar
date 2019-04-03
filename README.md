# WaveToolbar Brief Instruction
*Customized toolbar with bottom wave effect. More attactive animations will be added for wider selection.*

## How to integrate
*Step 1. Add the JitPack repository to your build file*
```
allprojects 
{
  repositories
  {
    ...
    maven { url 'https://jitpack.io' }
  }
}
```
 
*Step 2. Add the dependency*
```
dependencies
{
  implementation 'com.github.chimaeraqm:WaveToolbar:v0.2'
}
```
 
## Usage
### *Main Setter Method*

`setmBackground(Drawable mBackground)`

`setmTextView_Title(TextView mTextView_Title)`

`setmTitle(String mTitle)`

`setmTitleSize(int mTitleSize)`

`setmTitleColor(int mTitleColor)`

`setmAppIcon(Drawable mAppIcon)`

`setmAppIconShow(boolean mAppIconShow)`

`setmAppIconSize(int mAppIconSize)`

`setmBackIcon(Drawable mBackIcon)`

`setmBackIconSize(int mBackIconSize)`

`setmMenuIcon(Drawable mMenuIcon)`

`setmMenuIconSize(int mMenuIconSize)`

`setmBackgroundBlur(boolean mBackgroundBlur)`

`setmBackgroundBlurRatio(float mBackgroundBlurRatio)`

### *Main Click listener*
click listener interface for appicon, menuicon, backicon respectivly

`setBackIconClickListener(BackIconClickListener backIconClickListener)`

`setAppIconClickListener(AppIconClickListener appIconClickListener)`

`setMenuIconClickListener(MenuIconClickListener menuIconClickListener)`

### *Customizable attributes in .xml*
```
<attr name="viewBackground" format="reference"/>

<attr name="title" format="string"/>
<attr name="titleSize" format="dimension"/>
<attr name="titleColor" format="color"/>

<attr name="appIcon" format="reference"/>
<attr name="appIconShow" format="boolean"/>
<attr name="appIconSize" format="dimension"/>

<attr name="backIcon" format="reference"/>
<attr name="backIconSize" format="dimension"/>

<attr name="menuIcon" format="reference"/>
<attr name="menuIconShow" format="boolean"/>
<attr name="mentIconSize" format="dimension"/>

<attr name="backgroundBlur" format="boolean"/>
<attr name="backgroundBlurRatio" format="float"/>

<attr name="wave_Amlitude" format="integer" />
<attr name="wave_Speed" format="float" />
<attr name="firstWaveColor" format="color" />
<attr name="secondWaveColor" format="color" />
```

## License

```
Copyright 2019 Chimaeraqm

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```
