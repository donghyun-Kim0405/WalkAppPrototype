package com.example.navermapexample.history

import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.navermapexample.R
import com.example.navermapexample.databinding.FragmentHistoryBinding
import com.example.navermapexample.room.RouteEntity

class HistoryFragment : Fragment() {

    interface Listener{
        fun onRouteSelected(item: RouteEntity)
    }


    private lateinit var binding: FragmentHistoryBinding
    private val viewModel: HistoryViewModel by lazy { ViewModelProvider(this).get(HistoryViewModel::class.java) }

    private var listener: Listener? = null

    override fun onAttach(context: Context) {
        super.onAttach(context)
        listener = context as Listener?
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_history, container, false)
        setRecyclerView()
        setObserver()
        binding.btnFind.setOnClickListener {
            viewModel.getAllRoute()
        }

        return binding.root
    }//onCreateView()
    private fun setRecyclerView(){
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.recyclerView.adapter = RouteAdapter(ArrayList())
    }

    private fun setObserver(){
        viewModel.routeList.observe(viewLifecycleOwner, Observer{

            binding.recyclerView.adapter = RouteAdapter(it as ArrayList<RouteEntity>)
        })
    }


    inner class RouteHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val name: TextView = itemView.findViewById<TextView>(R.id.text_name)
        val distance: TextView = itemView.findViewById<TextView>(R.id.text_distance)
        val btn_getRoute: Button = itemView.findViewById(R.id.btn_getRoute)

        public fun bind(item : RouteEntity){
            name.text = item.name
            distance.text = item.distance.toString()
            btn_getRoute.setOnClickListener {
                listener?.onRouteSelected(item)
            }
        }
    }

    inner class RouteAdapter(val items: ArrayList<RouteEntity>) : RecyclerView.Adapter<RouteHolder>() {
        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RouteHolder {
            val view = layoutInflater.inflate(R.layout.item_route_layout, parent, false)
            return RouteHolder(view)
        }

        override fun onBindViewHolder(holder: RouteHolder, position: Int) {
            holder.bind(items.get(position))
        }

        override fun getItemCount(): Int = items.size

    }


    companion object{
        public fun getInstance(): HistoryFragment {
            return HistoryFragment()
        }

    }
}