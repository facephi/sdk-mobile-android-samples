package com.facephi.onboarding

import com.facephi.core.data.SdkApplication
import com.facephi.core.data.flow.FlowConfigurationData
import com.facephi.sdk.data.EnvironmentLicensingData
import com.facephi.sdk.data.LicensingOffline
import com.facephi.sdk.data.LicensingOnline
import com.facephi.sdk.data.SdkConfigurationData
import com.facephi.selphi_component.FSelphiController
import com.facephi.selphid.extractorcore.DocumentExtractor
import com.facephi.selphid.extractorv1.DocumentExtractorV1
import com.facephi.selphid.extractorv3.DocumentExtractorV3
import com.facephi.selphid_component.FSelphIDController
import com.facephi.tracking_component.TrackingController
import com.facephi.video_recording_component.FStopVideoRecordingController
import com.facephi.video_recording_component.FVideoRecordingController

object SdkData {

    // ************** LICENSE **************

    val environmentLicensingData: EnvironmentLicensingData = EnvironmentLicensingData(
        apiKey = "..."
    )

    // ************** DATA **************

    const val CUSTOMER_ID = "demo_onboarding_idv@email.com"

    // SelphID extractors: defaults match SelphIDExtractorSelectionConfiguration's preferredVendors (V3, V1)
    val selphIdExtractors: List<DocumentExtractor> = listOf(DocumentExtractorV3(), DocumentExtractorV1())

    fun getInitConfiguration(sdkApplication: SdkApplication) = SdkConfigurationData(
        sdkApplication = sdkApplication,
        licensing = LicensingOnline(environmentLicensingData),
        trackingController = TrackingController()
    )


    fun getIDVFlowConfigurationData(flowId: String): FlowConfigurationData {
        return FlowConfigurationData(
            id = flowId,
            controllers = listOf(
                FSelphiController(),
                FSelphIDController(extractors = selphIdExtractors),
                FStopVideoRecordingController(),
                FVideoRecordingController()
            ),
            customerId = CUSTOMER_ID
        )
    }

}