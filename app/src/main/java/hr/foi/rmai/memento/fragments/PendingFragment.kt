package hr.foi.rmai.memento.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import hr.foi.rmai.memento.R
import hr.foi.rmai.memento.adapters.TasksAdapter
import hr.foi.rmai.memento.helpers.MockDataLoader

class PendingFragment : Fragment() {
    private val mockData = MockDataLoader.getMockData()

    private lateinit var recyclerView: RecyclerView
    private lateinit var btnCreateTask: FloatingActionButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_pending, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        recyclerView = view.findViewById(R.id.rv_pending_tasks)
        recyclerView.adapter = TasksAdapter(mockData)
        recyclerView.layoutManager = LinearLayoutManager(view.context)

        btnCreateTask = view.findViewById(R.id.fab_pending_fragment_create_task)
        btnCreateTask.setOnClickListener {

        }
    }
}