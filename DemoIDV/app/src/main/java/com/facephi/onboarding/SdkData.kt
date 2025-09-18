package com.facephi.onboarding

import com.facephi.core.data.SdkApplication
import com.facephi.core.data.flow.FlowConfigurationData
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphi_component.FSelphiController
import com.facephi.selphid_component.FSelphIDController
import com.facephi.tracking_component.TrackingController

object SdkData {

    // ************** LICENSE **************

    const val LICENSE_ONLINE = true

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    const val LICENSE = """
        ...
    """

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_onboarding_idv@email.com"
    const val IDV_ID = "..."

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = if (LICENSE_ONLINE) {
            LicensingOnline(environmentLicensingData)
        } else {
            LicensingOffline(LICENSE)
        },
        activateFlow = true,
        trackingController = TrackingController()
    )


    fun getIDVFlowConfigurationData(): FlowConfigurationData {
        return FlowConfigurationData(
            id = IDV_ID,
            controllers = listOf(
                FSelphiController(),
                FSelphIDController()
            ),
            customerId = CUSTOMER_ID
        )
    }

}