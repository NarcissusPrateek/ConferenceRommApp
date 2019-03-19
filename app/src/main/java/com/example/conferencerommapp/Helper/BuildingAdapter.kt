package com.example.conferencerommapp.Helper



import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import com.example.conferencerommapp.Model.Building
import com.example.conferencerommapp.R



class BuildingAdapter(private val buildingList: List<Building>,val btnlistener: BtnClickListener) : androidx.recyclerview.widget.RecyclerView.Adapter<BuildingAdapter.ViewHolder>() {

	companion object {
		var mClickListener: BtnClickListener? = null

	 }

	override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {

		val view = LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
		return ViewHolder(view)
	}

	override fun onBindViewHolder(holder: ViewHolder, position: Int) {
		mClickListener = btnlistener
		holder.building = buildingList[position]
		holder.txvBuilding.text = buildingList[position].building_name
		var id = buildingList[position].building_id
		var building_name = buildingList[position].building_name
		holder.itemView.setOnClickListener { v ->
			mClickListener?.onBtnClick(id, building_name)
		}
	}
	override fun getItemCount(): Int {
		return buildingList.size
	}
	class ViewHolder(itemView: View) : androidx.recyclerview.widget.RecyclerView.ViewHolder(itemView) {
		val txvBuilding: TextView = itemView.findViewById(R.id.txv_building)
		var building: Building? = null
		override fun toString(): String {
			return """${super.toString()} '${txvBuilding.text}'"""
		}
	}

	open interface BtnClickListener {
		fun onBtnClick(buildingId: String?,buildingname: String?)
	}
}
