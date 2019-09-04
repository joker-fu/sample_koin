package com.joker.koin.di

import com.joker.koin.model.Girl
import com.joker.koin.model.ScopeEntity
import org.koin.core.qualifier.named
import org.koin.dsl.module

val girlModule = module {
    factory {
        Girl()
    }

    single(named("girl1")) { (type: String) ->
        Girl().apply {
            this.type = type
        }
    }

    single(named("girl2")) { (type: String) ->
        Girl().apply {
            this.type = type
        }
    }

    scope(named("scope")) {
        scoped {
            ScopeEntity()
        }
    }
}


//val girlModule1 = module {
//    single(named("girl1")) { (type: String) ->
//        Girl().apply {
//            this.type = type
//        }
//    }
//    single(named("girl2")) { (type: String) ->
//        Girl().apply {
//            this.type = type
//        }
//    }
//}