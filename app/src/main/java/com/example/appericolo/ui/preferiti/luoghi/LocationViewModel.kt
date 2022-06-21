package com.example.appericolo.ui.preferiti.luoghi

import androidx.lifecycle.*
import com.example.appericolo.ui.preferiti.luoghi.database.Location
import com.google.android.gms.maps.model.LatLng
import kotlinx.coroutines.launch

class LocationViewModel(private val repository: LocationRepository) : ViewModel() {

    // Using LiveData and caching what allLocations returns has several benefits:
    // - We can put an observer on the data (instead of polling for changes) and only update the
    //   the UI when the data actually changes.
    // - Repository is completely separated from the UI through the ViewModel.

    val allLocations: LiveData<List<Location>> = repository.allLocations.asLiveData()


    fun insert(location: Location) = viewModelScope.launch {
        repository.insert(location)
    }


    fun delete(location: Location) = viewModelScope.launch {
        repository.delete(location)
    }

    fun deleteAllLocations() = viewModelScope.launch {
        repository.deleteAllLocations()
    }

    //firebase query
    fun insertCurrentLocation(lastLocation: android.location.Location){
       repository.insertCurrentLocation(LatLng(lastLocation.latitude,
                   lastLocation.longitude))

           //}
       }
}

class LocationViewModelFactory(private val repository: LocationRepository) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LocationViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LocationViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}