package com.aifirewall.app.presentation.screens.profiles

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.aifirewall.app.data.local.db.entity.FirewallProfileEntity
import com.aifirewall.app.data.repository.ProfileRepository
import com.aifirewall.app.presentation.screens.security.RequireAuthentication
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfilesViewModel(private val repository: ProfileRepository) : ViewModel() {
    private val _profiles = MutableStateFlow<List<FirewallProfileEntity>>(emptyList())
    val profiles: StateFlow<List<FirewallProfileEntity>> = _profiles

    init {
        viewModelScope.launch {
            repository.getAllProfilesFlow().collect {
                _profiles.value = it
            }
        }
    }

    fun createProfile(name: String, description: String = "") {
        viewModelScope.launch { repository.createProfile(name, description) }
    }

    fun duplicateProfile(id: String, newName: String) {
        viewModelScope.launch { repository.duplicateProfile(id, newName) }
    }

    fun deleteProfile(profile: FirewallProfileEntity) {
        if (!profile.isActive) {
            viewModelScope.launch { repository.deleteProfile(profile) }
        }
    }

    fun activateProfile(id: String) {
        viewModelScope.launch { repository.activateProfile(id) }
    }

    class Factory(private val repo: ProfileRepository) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel> create(modelClass: Class<T>): T = ProfilesViewModel(repo) as T
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfilesScreen(navController: NavController) {
    val context = LocalContext.current
    val repo = remember { ProfileRepository(context) }
    val viewModel: ProfilesViewModel = viewModel(factory = ProfilesViewModel.Factory(repo))
    val profiles by viewModel.profiles.collectAsState()

    var showCreateDialog by remember { mutableStateOf(false) }
    var actionToAuthenticate by remember { mutableStateOf<(() -> Unit)?>(null) }

    if (actionToAuthenticate != null) {
        RequireAuthentication(
            onAuthenticated = {
                actionToAuthenticate?.invoke()
                actionToAuthenticate = null
            },
            onCancel = { actionToAuthenticate = null }
        ) {}
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Firewall Profiles", fontWeight = FontWeight.Bold) },
                actions = {
                    IconButton(onClick = { actionToAuthenticate = { showCreateDialog = true } }) {
                        Icon(Icons.Default.Add, contentDescription = "Add Profile")
                    }
                }
            )
        }
    ) { padding ->
        LazyColumn(
            modifier = Modifier.fillMaxSize().padding(padding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(profiles, key = { it.id }) { profile ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    Row(
                        modifier = Modifier.fillMaxWidth().padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(profile.name, style = MaterialTheme.typography.titleMedium, fontWeight = FontWeight.Bold)
                                if (profile.isActive) {
                                    Spacer(Modifier.width(8.dp))
                                    Icon(Icons.Default.CheckCircle, contentDescription = "Active", tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(20.dp))
                                }
                            }
                            if (profile.description.isNotBlank()) {
                                Text(profile.description, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                            }
                        }
                        
                        IconButton(onClick = {
                            actionToAuthenticate = { viewModel.duplicateProfile(profile.id, "${profile.name} (Copy)") }
                        }) {
                            Icon(Icons.Default.Edit, contentDescription = "Duplicate")
                        }

                        if (!profile.isActive) {
                            IconButton(onClick = {
                                actionToAuthenticate = { viewModel.activateProfile(profile.id) }
                            }) {
                                Icon(Icons.Default.Check, contentDescription = "Activate")
                            }
                            IconButton(onClick = {
                                actionToAuthenticate = { viewModel.deleteProfile(profile) }
                            }) {
                                Icon(Icons.Default.Delete, contentDescription = "Delete", tint = MaterialTheme.colorScheme.error)
                            }
                        }
                    }
                }
            }
        }
    }

    if (showCreateDialog) {
        var name by remember { mutableStateOf("") }
        var desc by remember { mutableStateOf("") }
        AlertDialog(
            onDismissRequest = { showCreateDialog = false },
            title = { Text("Create Profile") },
            text = {
                Column {
                    OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, singleLine = true)
                    Spacer(Modifier.height(8.dp))
                    OutlinedTextField(value = desc, onValueChange = { desc = it }, label = { Text("Description") }, singleLine = true)
                }
            },
            confirmButton = {
                Button(onClick = {
                    if (name.isNotBlank()) {
                        viewModel.createProfile(name, desc)
                        showCreateDialog = false
                    }
                }) { Text("Create") }
            },
            dismissButton = {
                TextButton(onClick = { showCreateDialog = false }) { Text("Cancel") }
            }
        )
    }
}
