package com.steadytech.impian.model.firebase

import com.steadytech.impian.helper.Constant

data class Wishlist(
   var id: String = "" ,
   var amount: String = "",
   var category: String = "",
   var completed: String = "" ,
   var description: String = "",
   var startDate: String = "" ,
   var endDate: String = "" ,
   var name: String = "" ,
   var reminderInterval: String = ""
) {}
