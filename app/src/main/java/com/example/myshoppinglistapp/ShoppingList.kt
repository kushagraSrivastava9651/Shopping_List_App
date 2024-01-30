package com.example.myshoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(val id:Int ,var name:String, var quantity:Int , var isEditing:Boolean){

}



@Composable
fun ShoppingListApp(){
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showDialogs by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var quantity by remember {
        mutableStateOf("")
    }


    Column (
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center
    ){
        Button(onClick = { showDialogs=true },
            modifier = Modifier.align(Alignment.CenterHorizontally)) {
            Text(text = "Add Item")
        }
        LazyColumn(modifier = Modifier
            .fillMaxSize()
            .padding(16.dp) ){
            items(sItems){
                item->
                if(item.isEditing){
                    ShoppingAppEditor(item = item, onEditComplete = {editName,editQuantity->sItems=sItems.map {it.copy(isEditing = false)  }
                    val editedItem=sItems.find { it.id==item.id }
                        editedItem?.let {
                            it.name=editName
                            it.quantity=editQuantity
                        }
                    })
                }
                else{
                    ShoppingListItem(item = item, onEdit = {  sItems=sItems.map { it.copy(isEditing = it.id==item.id)
                    } }, onDelete = {sItems=sItems-item})
                        

                }
            }

        }

    }
    
    if(showDialogs){
       AlertDialog(onDismissRequest = { showDialogs=false },
           confirmButton = {
           Row(modifier = Modifier
               .fillMaxWidth()
               .padding(8.dp),
               horizontalArrangement = Arrangement.SpaceBetween) {
                 Button(onClick = {
                     if(itemName.isNotBlank()){
                         val newItem=ShoppingItem(id=sItems.size+1,name=itemName,quantity=quantity.toInt(),false)
                         sItems=sItems+newItem
                         showDialogs=false
                         itemName=""

                     }
                 }) {
                     Text(text = "Add")
                 }
               Button(onClick = { showDialogs=false }) {
                   Text(text = "Cancel")
               }
           }
       },
           title = {Text("ADD SHOPPING ITEMS")},
           text = { Column {
             OutlinedTextField(value = itemName,
                 onValueChange = {itemName=it},
                 singleLine = true,
                 modifier = Modifier
                     .fillMaxWidth()
                     .padding(8.dp)
             )

               OutlinedTextField(
                   value = quantity,
                   onValueChange = {quantity=it},
                   singleLine = true,
                   modifier = Modifier
                       .fillMaxWidth()
                       .padding(8.dp)
               )
           }
           }
       )




    }
    
}






@Composable
fun ShoppingListItem(item: ShoppingItem , onEdit:()->Unit, onDelete:()->Unit){
Row (modifier =
Modifier
    .padding(8.dp)
    .fillMaxWidth()
    .border(
        border = BorderStroke(2.dp, Color(0XFF018789)),
        shape = RoundedCornerShape(20)
    ), horizontalArrangement = Arrangement.SpaceBetween){
Text(text = item.name, modifier = Modifier.padding(8.dp))
    Text(text ="QTY:  ${item.quantity}", modifier = Modifier.padding(8.dp))
    Row (modifier = Modifier.padding(8.dp)){
       IconButton(onClick = {  onEdit() }) {
           Icon(imageVector = Icons.Default.Edit, contentDescription =null )
           
       }
        IconButton(onClick = {  onDelete() }) {
            Icon(imageVector = Icons.Default.Delete, contentDescription =null )

        }

    }
}
}



@Composable
fun ShoppingAppEditor(item: ShoppingItem,onEditComplete: (String,Int) -> Unit){
    var editName by remember {
        mutableStateOf(item.name)
    }
    var quantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }
    Row(modifier = Modifier
        .fillMaxWidth()
        .padding(8.dp)
        .background(Color.White), horizontalArrangement = Arrangement.SpaceEvenly) {
    Column {
        BasicTextField(value = editName, onValueChange = {editName=it}, singleLine = true, modifier = Modifier
            .wrapContentSize()
            .padding(8.dp))
        BasicTextField(value = quantity, onValueChange = {quantity=it}, singleLine = true, modifier = Modifier
            .wrapContentSize()
            .padding(8.dp))
        
    }
        Button(onClick = { isEditing=false
        onEditComplete(editName,quantity.toIntOrNull()?:1)}) {
            Text(text = "Save")
        }
    }
}