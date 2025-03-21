package hr.foi.rmai.memento.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.TasksAdapter
import hr.foi.rmai.memento.helpers.MockDataLoader

class PendingFragment : Fragment() {
    private val mockData = MockDataLoader.getMockData()

    private lateinit var recylerView: RecyclerView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recylerView = view.findViewById(R.id.rv_pending_tasks)
        recylerView.adapter = TasksAdapter(mockData)
        recylerView.layoutManager = LinearLayoutManager(view.context)
    }
}