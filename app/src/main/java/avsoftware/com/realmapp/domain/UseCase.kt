package avsoftware.com.realmapp.domain

import io.reactivex.Observable

interface UseCase<REQUEST,RESPONSE> {
    fun execute(  request: REQUEST ): Observable<RESPONSE>
}