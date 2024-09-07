/*
 * Copyright (c) 2024. RW MobiMedia UK Limited
 *
 * Contributions made by other developers remain the property of their respective authors but are licensed
 * to RW MobiMedia UK Limited and others under the same licence terms as the main project, as outlined in
 * the LICENSE file.
 *
 * RW MobiMedia UK Limited reserves the exclusive right to distribute this application on app stores.
 * Reuse of this source code, with or without modifications, requires proper attribution to
 * RW MobiMedia UK Limited.  Commercial distribution of this code or its derivatives without prior written
 * permission from RW MobiMedia UK Limited is prohibited.
 *
 * Please refer to the LICENSE file for the full terms and conditions.
 */

import com.rwmobi.kunigami.domain.model.consumption.Consumption
import kotlinx.datetime.Instant

object ConsumptionSampleData {
    val randomSample = listOf(
        Consumption(kWhConsumed = 0.165235958596004, interval = Instant.parse("2024-08-22T23:00:00Z")..Instant.parse("2024-08-22T23:30:00Z")),
        Consumption(kWhConsumed = 0.09940452763221952, interval = Instant.parse("2024-08-22T23:30:00Z")..Instant.parse("2024-08-23T00:00:00Z")),
        Consumption(kWhConsumed = 0.44884703198248777, interval = Instant.parse("2024-08-23T00:00:00Z")..Instant.parse("2024-08-23T00:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T00:30:00Z")..Instant.parse("2024-08-23T01:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T01:00:00Z")..Instant.parse("2024-08-23T01:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T01:30:00Z")..Instant.parse("2024-08-23T02:00:00Z")),
        Consumption(kWhConsumed = 0.28573133929126837, interval = Instant.parse("2024-08-23T02:00:00Z")..Instant.parse("2024-08-23T02:30:00Z")),
        Consumption(kWhConsumed = 0.5097595736386005, interval = Instant.parse("2024-08-23T02:30:00Z")..Instant.parse("2024-08-23T03:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T03:00:00Z")..Instant.parse("2024-08-23T03:30:00Z")),
        Consumption(kWhConsumed = 0.3033163991134752, interval = Instant.parse("2024-08-23T03:30:00Z")..Instant.parse("2024-08-23T04:00:00Z")),
        Consumption(kWhConsumed = 0.185290486549797, interval = Instant.parse("2024-08-23T04:00:00Z")..Instant.parse("2024-08-23T04:30:00Z")),
        Consumption(kWhConsumed = 0.5469903088481638, interval = Instant.parse("2024-08-23T04:30:00Z")..Instant.parse("2024-08-23T05:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T05:00:00Z")..Instant.parse("2024-08-23T05:30:00Z")),
        Consumption(kWhConsumed = 0.15303057480613907, interval = Instant.parse("2024-08-23T05:30:00Z")..Instant.parse("2024-08-23T06:00:00Z")),
        Consumption(kWhConsumed = 0.5572410421395602, interval = Instant.parse("2024-08-23T06:00:00Z")..Instant.parse("2024-08-23T06:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T06:30:00Z")..Instant.parse("2024-08-23T07:00:00Z")),
        Consumption(kWhConsumed = 0.07875303147828325, interval = Instant.parse("2024-08-23T07:00:00Z")..Instant.parse("2024-08-23T07:30:00Z")),
        Consumption(kWhConsumed = 0.17521369917222113, interval = Instant.parse("2024-08-23T07:30:00Z")..Instant.parse("2024-08-23T08:00:00Z")),
        Consumption(kWhConsumed = 0.41243018799704745, interval = Instant.parse("2024-08-23T08:00:00Z")..Instant.parse("2024-08-23T08:30:00Z")),
        Consumption(kWhConsumed = 0.407120707203384, interval = Instant.parse("2024-08-23T08:30:00Z")..Instant.parse("2024-08-23T09:00:00Z")),
        Consumption(kWhConsumed = 0.3084046466838003, interval = Instant.parse("2024-08-23T09:00:00Z")..Instant.parse("2024-08-23T09:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T09:30:00Z")..Instant.parse("2024-08-23T10:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T10:00:00Z")..Instant.parse("2024-08-23T10:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T10:30:00Z")..Instant.parse("2024-08-23T11:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T11:00:00Z")..Instant.parse("2024-08-23T11:30:00Z")),
        Consumption(kWhConsumed = 0.24064461934675496, interval = Instant.parse("2024-08-23T11:30:00Z")..Instant.parse("2024-08-23T12:00:00Z")),
        Consumption(kWhConsumed = 0.08438257327746373, interval = Instant.parse("2024-08-23T12:00:00Z")..Instant.parse("2024-08-23T12:30:00Z")),
        Consumption(kWhConsumed = 0.19205673611093207, interval = Instant.parse("2024-08-23T12:30:00Z")..Instant.parse("2024-08-23T13:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T13:00:00Z")..Instant.parse("2024-08-23T13:30:00Z")),
        Consumption(kWhConsumed = 0.3250650615004777, interval = Instant.parse("2024-08-23T13:30:00Z")..Instant.parse("2024-08-23T14:00:00Z")),
        Consumption(kWhConsumed = 0.45494789287668, interval = Instant.parse("2024-08-23T14:00:00Z")..Instant.parse("2024-08-23T14:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T14:30:00Z")..Instant.parse("2024-08-23T15:00:00Z")),
        Consumption(kWhConsumed = 0.1980893484281169, interval = Instant.parse("2024-08-23T15:00:00Z")..Instant.parse("2024-08-23T15:30:00Z")),
        Consumption(kWhConsumed = 0.2229933747408947, interval = Instant.parse("2024-08-23T15:30:00Z")..Instant.parse("2024-08-23T16:00:00Z")),
        Consumption(kWhConsumed = 0.17072542494030654, interval = Instant.parse("2024-08-23T16:00:00Z")..Instant.parse("2024-08-23T16:30:00Z")),
        Consumption(kWhConsumed = 0.12325363026046059, interval = Instant.parse("2024-08-23T16:30:00Z")..Instant.parse("2024-08-23T17:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T17:00:00Z")..Instant.parse("2024-08-23T17:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T17:30:00Z")..Instant.parse("2024-08-23T18:00:00Z")),
        Consumption(kWhConsumed = 0.1501275625524833, interval = Instant.parse("2024-08-23T18:00:00Z")..Instant.parse("2024-08-23T18:30:00Z")),
        Consumption(kWhConsumed = 0.3045590657185424, interval = Instant.parse("2024-08-23T18:30:00Z")..Instant.parse("2024-08-23T19:00:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T19:00:00Z")..Instant.parse("2024-08-23T19:30:00Z")),
        Consumption(kWhConsumed = 0.1510989296101421, interval = Instant.parse("2024-08-23T19:30:00Z")..Instant.parse("2024-08-23T20:00:00Z")),
        Consumption(kWhConsumed = 0.15901166048896162, interval = Instant.parse("2024-08-23T20:00:00Z")..Instant.parse("2024-08-23T20:30:00Z")),
        Consumption(kWhConsumed = 0.31650428825396915, interval = Instant.parse("2024-08-23T20:30:00Z")..Instant.parse("2024-08-23T21:00:00Z")),
        Consumption(kWhConsumed = 0.2239049103605202, interval = Instant.parse("2024-08-23T21:00:00Z")..Instant.parse("2024-08-23T21:30:00Z")),
        Consumption(kWhConsumed = 0.05, interval = Instant.parse("2024-08-23T21:30:00Z")..Instant.parse("2024-08-23T22:00:00Z")),
        Consumption(kWhConsumed = 0.3622933497727502, interval = Instant.parse("2024-08-23T22:00:00Z")..Instant.parse("2024-08-23T22:30:00Z")),
        Consumption(kWhConsumed = 0.2349476006387274, interval = Instant.parse("2024-08-23T22:30:00Z")..Instant.parse("2024-08-23T23:00:00Z")),
    )
}
