/* DO NOT EDIT THIS FILE - it is machine generated */
#include <jni.h>
/* Header for class javabot_JNIBWAPI */

#ifndef _Included_javabot_JNIBWAPI
#define _Included_javabot_JNIBWAPI
#ifdef __cplusplus
extern "C" {
#endif
/*
 * Class:     javabot_JNIBWAPI
 * Method:    startClient
 * Signature: (Ljavabot/JNIBWAPI;)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_startClient
  (JNIEnv *, jobject, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getGameFrame
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_javabot_JNIBWAPI_getGameFrame
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getPlayerInfo
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getPlayerInfo
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getPlayerUpdate
 * Signature: (I)[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getPlayerUpdate
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getResearchStatus
 * Signature: (I)[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getResearchStatus
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUpgradeStatus
 * Signature: (I)[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getUpgradeStatus
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnits
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getUnits
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnitTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getUnitTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnitTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getUnitTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getTechTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getTechTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getTechTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getTechTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUpgradeTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getUpgradeTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUpgradeTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getUpgradeTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getWeaponTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getWeaponTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getWeaponTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getWeaponTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnitSizeTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getUnitSizeTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnitSizeTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getUnitSizeTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getBulletTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getBulletTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getBulletTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getBulletTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getDamageTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getDamageTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getDamageTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getDamageTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getExplosionTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getExplosionTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getExplosionTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getExplosionTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnitCommandTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getUnitCommandTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getUnitCommandTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getUnitCommandTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getOrderTypes
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getOrderTypes
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getOrderTypeName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getOrderTypeName
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    analyzeTerrain
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_analyzeTerrain
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getMapWidth
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_javabot_JNIBWAPI_getMapWidth
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getMapHeight
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_javabot_JNIBWAPI_getMapHeight
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getMapName
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getMapName
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getMapHash
 * Signature: ()Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getMapHash
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getHeightData
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getHeightData
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getWalkableData
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getWalkableData
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getBuildableData
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getBuildableData
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getChokePoints
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getChokePoints
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getRegions
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getRegions
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getPolygon
 * Signature: (I)[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getPolygon
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getBaseLocations
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getBaseLocations
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    attack
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_attack__III
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    attack
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_attack__II
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    build
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_build
  (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    buildAddon
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_buildAddon
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    train
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_train
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    morph
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_morph
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    research
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_research
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    upgrade
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_upgrade
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    setRallyPoint
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_setRallyPoint__III
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    setRallyPoint
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_setRallyPoint__II
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    move
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_move
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    patrol
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_patrol
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    holdPosition
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_holdPosition
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    stop
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_stop
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    follow
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_follow
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    gather
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_gather
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    returnCargo
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_returnCargo
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    repair
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_repair
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    burrow
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_burrow
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    unburrow
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_unburrow
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cloak
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cloak
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    decloak
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_decloak
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    siege
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_siege
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    unsiege
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_unsiege
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    lift
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_lift
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    land
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_land
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    load
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_load
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    unload
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_unload
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    unloadAll
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_unloadAll__I
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    unloadAll
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_unloadAll__III
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    rightClick
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_rightClick__III
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    rightClick
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_rightClick__II
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    haltConstruction
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_haltConstruction
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cancelConstruction
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cancelConstruction
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cancelAddon
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cancelAddon
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cancelTrain
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cancelTrain
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cancelMorph
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cancelMorph
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cancelResearch
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cancelResearch
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    cancelUpgrade
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_cancelUpgrade
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    useTech
 * Signature: (II)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_useTech__II
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    useTech
 * Signature: (IIII)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_useTech__IIII
  (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    useTech
 * Signature: (III)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_useTech__III
  (JNIEnv *, jobject, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawHealth
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawHealth
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawTargets
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawTargets
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawIDs
 * Signature: (Z)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawIDs
  (JNIEnv *, jobject, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    enableUserInput
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_enableUserInput
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    enablePerfectInformation
 * Signature: ()V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_enablePerfectInformation
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    setGameSpeed
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_setGameSpeed
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawBox
 * Signature: (IIIIIZZ)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawBox
  (JNIEnv *, jobject, jint, jint, jint, jint, jint, jboolean, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawCircle
 * Signature: (IIIIZZ)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawCircle
  (JNIEnv *, jobject, jint, jint, jint, jint, jboolean, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawLine
 * Signature: (IIIIIZ)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawLine
  (JNIEnv *, jobject, jint, jint, jint, jint, jint, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawDot
 * Signature: (IIIZ)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawDot
  (JNIEnv *, jobject, jint, jint, jint, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    drawText
 * Signature: (IILjava/lang/String;Z)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_drawText
  (JNIEnv *, jobject, jint, jint, jstring, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    hasCreep
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_hasCreep
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    canBuildHere
 * Signature: (IIIIZ)Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_canBuildHere
  (JNIEnv *, jobject, jint, jint, jint, jint, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    printText
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_printText
  (JNIEnv *, jobject, jstring);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    sendText
 * Signature: (Ljava/lang/String;)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_sendText
  (JNIEnv *, jobject, jstring);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    setCommandOptimizationLevel
 * Signature: (I)V
 */
JNIEXPORT void JNICALL Java_javabot_JNIBWAPI_setCommandOptimizationLevel
  (JNIEnv *, jobject, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    isReplay
 * Signature: ()Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_isReplay
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getShortestPath
 * Signature: (IIII)[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getShortestPath
  (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getGroundDistance
 * Signature: (IIII)D
 */
JNIEXPORT jdouble JNICALL Java_javabot_JNIBWAPI_getGroundDistance
  (JNIEnv *, jobject, jint, jint, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    isVisible
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_isVisible
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    isExplored
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_isExplored
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    isBuildable
 * Signature: (IIZ)Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_isBuildable
  (JNIEnv *, jobject, jint, jint, jboolean);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getLastError
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_javabot_JNIBWAPI_getLastError
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    hasLoadedUnit
 * Signature: (II)Z
 */
JNIEXPORT jboolean JNICALL Java_javabot_JNIBWAPI_hasLoadedUnit
  (JNIEnv *, jobject, jint, jint);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getRemainingLatencyFrames
 * Signature: ()I
 */
JNIEXPORT jint JNICALL Java_javabot_JNIBWAPI_getRemainingLatencyFrames
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getStaticNeutralUnits
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getStaticNeutralUnits
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getStaticMinerals
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getStaticMinerals
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getStaticGeysers
 * Signature: ()[I
 */
JNIEXPORT jintArray JNICALL Java_javabot_JNIBWAPI_getStaticGeysers
  (JNIEnv *, jobject);

/*
 * Class:     javabot_JNIBWAPI
 * Method:    getPlayerName
 * Signature: (I)Ljava/lang/String;
 */
JNIEXPORT jstring JNICALL Java_javabot_JNIBWAPI_getPlayerName
  (JNIEnv *, jobject, jint);

#ifdef __cplusplus
}
#endif
#endif
