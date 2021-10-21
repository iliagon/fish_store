package com.example.fishstore.config

import org.springframework.test.context.TestExecutionListeners
import com.example.fishstore.config.ResetDatabaseTestExecutionListener
import org.springframework.boot.test.context.SpringBootTest

@TestExecutionListeners(mergeMode = TestExecutionListeners.MergeMode.MERGE_WITH_DEFAULTS, listeners = [ResetDatabaseTestExecutionListener::class])
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class CreateOrderIT 