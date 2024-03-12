package com.facephi.demophingers


import com.facephi.core.data.OperationType
import com.facephi.sdk.data.EnvironmentLicensingData

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = false

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        url = "https://....dev",
        apiKey = "..."
    )

    const val LICENSE = ""

    // ************** DATA **************

    const val CUSTOMER_ID: String = "demo-phingers-android@email.com"
    val OPERATION_TYPE = OperationType.ONBOARDING

}