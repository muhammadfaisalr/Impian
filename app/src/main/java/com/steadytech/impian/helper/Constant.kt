package com.steadytech.impian.helper

class Constant  {
    class MODE {
        companion object{
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
}