package com.yuriisurzhykov.pointdetector.presentation.points.list

import android.content.Context
import android.content.Intent
import android.location.Location
import android.os.Bundle
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import androidx.core.view.MenuProvider
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.BaseTransientBottomBar
import com.google.android.material.snackbar.Snackbar
import com.yuriisurzhykov.pointdetector.R
import com.yuriisurzhykov.pointdetector.domain.entities.Point
import com.yuriisurzhykov.pointdetector.presentation.core.NavigationCallback
import com.yuriisurzhykov.pointdetector.presentation.map.AbstractLocationFragment
import com.yuriisurzhykov.pointdetector.presentation.points.create.PointsCreateActivity
import com.yuriisurzhykov.pointdetector.presentation.points.details.PointDetailsFragment
import com.yuriisurzhykov.pointsdetector.uicomponents.list.EmptyStateData
import com.yuriisurzhykov.pointsdetector.uicomponents.list.PointSwipeDeleteCallback
import com.yuriisurzhykov.pointsdetector.uicomponents.list.SwipeRecyclerCallbacks
import com.yuriisurzhykov.pointsdetector.uicomponents.list.ViewHolderItem
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class PointsListFragment : AbstractLocationFragment(R.layout.fragment_points_list) {

    private val viewModel: PointsListViewModel by viewModels()
    private val listAdapter = PointsListAdapter()
    private var navigationCallback: NavigationCallback? = null

    override fun getTitle(): CharSequence {
        return resources.getString(R.string.title_points_list_screen)
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        check(context is NavigationCallback) {
            throw IllegalStateException("Context must implement NavigationCallback")
        }
        navigationCallback = context
    }

    override fun onDetach() {
        super.onDetach()
        navigationCallback = null
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        with(view.findViewById<RecyclerView>(R.id.recycler)) {
            adapter = listAdapter
            layoutManager = LinearLayoutManager(context)
            ItemTouchHelper(
                PointSwipeDeleteCallback(
                    listAdapter,
                    view.context
                )
            ).attachToRecyclerView(this)
        }
        with(view.findViewById<EditText>(R.id.search_text_input)) {
            setText(viewModel.getSearchCondition())
            addTextChangedListener { viewModel.startLoadPoints(it.toString()) }
        }
        activity?.addMenuProvider(fragmentMenuProvider, viewLifecycleOwner)
        listAdapter.setOnItemClickListener { item: ViewHolderItem ->
            if (item is EmptyStateData) {
                onCreateNewPointClick()
            } else if (item is Point) {
                openPointDetailsFragment(item)
            }
        }
        listAdapter.setOnSwipeListener(object : SwipeRecyclerCallbacks<ViewHolderItem> {
            override fun onStartSwipe(viewHolder: RecyclerView.ViewHolder) {
                viewModel.setUpdatesAvailable(false)
            }

            override fun onSwipeReleased(viewHolder: RecyclerView.ViewHolder) {
                viewModel.setUpdatesAvailable(true)
            }

            override fun onSwiped(
                viewHolder: RecyclerView.ViewHolder,
                position: Int,
                item: ViewHolderItem
            ) {
                showOnDeleteSnackbar(item, position)
            }
        })
        viewModel.observePointsList(viewLifecycleOwner) {
            this@PointsListFragment.view?.post { listAdapter.submitList(it) }
        }
    }

    private fun showOnDeleteSnackbar(point: ViewHolderItem, position: Int) {
        view?.let {
            val snackbar = Snackbar
                .make(it, R.string.label_point_deletec_action, Snackbar.LENGTH_LONG)
                .setAction(R.string.button_undo) {
                    listAdapter.insertItem(point, position)
                }
            snackbar.addCallback(object : BaseTransientBottomBar.BaseCallback<Snackbar>() {
                override fun onDismissed(transientBottomBar: Snackbar?, event: Int) {
                    if (point is Point && event != DISMISS_EVENT_ACTION) {
                        viewModel.removeItem(point)
                    }
                    viewModel.setUpdatesAvailable(true)
                    snackbar.removeCallback(this)
                }
            })
            snackbar.show()
        }
    }

    private fun onCreateNewPointClick() {
        startActivity(Intent(activity, PointsCreateActivity::class.java))
    }

    override fun onStart() {
        super.onStart()
        checkPermissions()
        viewModel.startLoadPoints()
    }

    override fun onLocationReceived(location: Location) {
        viewModel.updateUserLocation(location)
    }

    private fun openPointDetailsFragment(item: Point) {
        openFragment(PointDetailsFragment.newInstance(item), "point_details_stack_name")
    }

    private val fragmentMenuProvider = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            menuInflater.inflate(R.menu.points_list_menu, menu)
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            if (menuItem.itemId == R.id.add_point) {
                onCreateNewPointClick()
                return true
            }
            return false
        }
    }

}