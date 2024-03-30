package com.cs4520.assignment5

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.cs4520.assignment5.databinding.ProductListFragmentBinding

class ProductListFragment : Fragment(R.layout.product_list_fragment) {
    private var _binding: ProductListFragmentBinding? = null
    private val binding get() = _binding!!

    private var productList: ProductList? = null

    private lateinit var viewModel: DataViewModel

    override fun onResume() {
        super.onResume()

        // Update data on resume
        viewModel.products.observe(viewLifecycleOwner, Observer { res ->
            _binding!!.progressBar.visibility = View.GONE // remove loading bar once query is done
            var productSet: ProductList = ProductList()
            if (res != null) {
                for (p in res.distinct()) {
                    productSet.add(p)
                }
            }

            productList = if (productSet.size > 0) productSet else res

            if (res == null) {
                _binding!!.errorText.text = getString(R.string.api_err)
                _binding!!.errorText.visibility = View.VISIBLE
            }
            else if (res.size == 0) {
                _binding!!.errorText.text = getString(R.string.no_res)
                _binding!!.errorText.visibility = View.VISIBLE
            } else {
                _binding!!.errorText.visibility = View.GONE
                (_binding!!.rvProducts.adapter as ProductListAdapter).setProducts(productList)
                _binding!!.rvProducts.adapter?.notifyDataSetChanged()
            }
        })

        viewModel.getProducts()
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        super.onCreateView(inflater, container, savedInstanceState)

        _binding = ProductListFragmentBinding.inflate(inflater, container, false)
        _binding!!.progressBar.visibility = View.VISIBLE // show loading wheel

        val rvProducts = _binding!!.rvProducts as RecyclerView

        val db = Room.databaseBuilder(
            requireContext(),
            AppDatabase::class.java, "product"
        ).build()
        val vm = DataViewModel(db)
        viewModel = vm


        // Create adapter using data from our Dataset
        val adapter = ProductListAdapter(productList, container)
        rvProducts.adapter = adapter // Bind our RecycleView to our custom Adapter
        rvProducts.layoutManager = LinearLayoutManager(this.context)

        return binding.root
    }
}