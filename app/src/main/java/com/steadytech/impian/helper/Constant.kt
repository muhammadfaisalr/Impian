package com.steadytech.impian.helper

class Constant  {
    class NAME {
        companion object{
            const val MODE_IS_ANONYMOUS = "MODE_IS_ANONYMOUS"
            const val LOCAL_DB = "IMPIAN-DATABASE"
        }
    }

    class MODE {
        companion object{
            const val ALL = "ALL"
            const val PROFILE = "PROFILE"
            const val SIGN_UP = "SIGN_UP"
            const val SIGN_IN = "SIGN_IN"
            const val EDIT = "EDIT"
            const val VIEW = "VIEW"
            const val ANONYMOUS = "ANONYMOUS"
        }
    }

    class TAG{
        companion object{
            const val AboutGoalsActivity = "AboutGoalsActivity"
            const val CreateGoalsActivity = "CreateGoalsActivity"
            const val DetailGoalsActivity = "DetailGoalsActivity"
        }
    }

    class REMINDER_INTERVAL {
        companion object{
            const val DAY = "DAY"
            const val WEEK = "WEEK"
            const val MONTH = "MONTH"
        }
    }

    class DATABASE_REFERENCE{
        companion object{
            const val USER = "USER"
        }
    }

    class KEY {
        companion object{
            const val MODE = "MODE"
            const val ID_WISHLIST = "ID_WISHLIST"
        }
    }

    class SHARED_PREFERENCES {
        companion object{
            const val GLOBAL_NAME = "IMPIAN"
        }
    }

    class PATH {
        companion object{
            const val IMPIAN = "IMPIAN"
        }
    }
}