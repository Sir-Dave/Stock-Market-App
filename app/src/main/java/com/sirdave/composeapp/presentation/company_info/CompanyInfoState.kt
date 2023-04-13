package com.sirdave.composeapp.presentation.company_info

import com.sirdave.composeapp.domain.model.CompanyInfo
import com.sirdave.composeapp.domain.model.IntradayInfo

data class CompanyInfoState(
    val stockInfos: List<IntradayInfo> = emptyList(),
    val company: CompanyInfo? = null,
    val isLoading: Boolean = false,
    val error: String? = null
)
