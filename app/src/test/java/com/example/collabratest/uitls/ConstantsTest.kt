package com.example.collabratest.uitls

import org.junit.Assert.*
import org.junit.Before

import org.junit.Test

class ConstantsTest {
    lateinit var constants: Constants

    @Before
    fun setUp(){
        constants = Constants
    }
    @Test
    fun isValidEmail() {
        val result = constants.isValidEmail("anil@gmail.com")
        assertEquals(true,result)
    }

}