<?xml version="1.0" encoding="UTF-8"?>
<tileset name="lol" tilewidth="128" tileheight="128" tilecount="7" columns="0">
 <grid orientation="orthogonal" width="1" height="1"/>
 <tile id="0" type="block">
  <image width="128" height="128" source="../Obstacles/blockade.png"/>
  <objectgroup draworder="index">
   <object id="2" x="9.84615" y="9.84615" width="109.385" height="109.231"/>
  </objectgroup>
 </tile>
 <tile id="1" type="mirror">
  <image width="128" height="128" source="../Obstacles/mirror.png"/>
  <objectgroup draworder="index">
   <object id="7" x="10.1538" y="9.07692">
    <polygon points="0,0 109.538,109.692 -0.461538,109.846"/>
   </object>
  </objectgroup>
 </tile>
 <tile id="2" type="pelletstart">
  <properties>
   <property name="fireallatonce" type="bool" value="false"/>
   <property name="firedirections" value=""/>
   <property name="initialdelay" type="float" value="999"/>
   <property name="pelletpause" type="float" value="999"/>
  </properties>
  <image width="128" height="128" source="../Obstacles/start.png"/>
 </tile>
 <tile id="3" type="pelletend">
  <image width="128" height="128" source="../Obstacles/endSmall.png"/>
 </tile>
 <tile id="4" type="toggle">
  <properties>
   <property name="id" type="int" value="999"/>
   <property name="isbutton" type="bool" value="false"/>
   <property name="startson" type="bool" value="false"/>
  </properties>
  <image width="83" height="83" source="../Obstacles/toggleBlank.png"/>
 </tile>
 <tile id="5" type="trackpoint">
  <properties>
   <property name="groupcolor" type="color" value=""/>
   <property name="groupnum" type="int" value="999"/>
   <property name="id" type="int" value="999"/>
  </properties>
  <image width="64" height="64" source="../Obstacles/Track/trackPoint.png"/>
 </tile>
 <tile id="6" type="wallportal">
  <properties>
   <property name="id" type="int" value="999"/>
   <property name="linkedid" type="int" value="999"/>
   <property name="side" value=""/>
  </properties>
  <image width="128" height="128" source="../Obstacles/PORTALTILE.png"/>
 </tile>
</tileset>
