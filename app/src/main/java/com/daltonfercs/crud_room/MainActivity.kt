package com.daltonfercs.crud_room

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.room.Room
import com.daltonfercs.crud_room.databinding.ActivityMainBinding
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity(), AdaptadorListener {

    lateinit var binding: ActivityMainBinding

    var listaUsuarios: MutableList<Usuario> = mutableListOf()
    lateinit var adaptador: AdaptadorUsuarios
    lateinit var room:DBPrueba
    lateinit var usuario:Usuario

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.rvUsuarios.layoutManager = LinearLayoutManager(this)
        room = Room.databaseBuilder(this, DBPrueba::class.java, "dbPruebas").build()
        obtenerUsuarios(room)

        binding.btnAddUpdate.setOnClickListener {
            if (binding.etUsuario.text.isNullOrEmpty() || binding.etPais.text.isNullOrEmpty()) {
                Toast.makeText(this, "Debes llenar todos los campos", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (binding.btnAddUpdate.text.equals("agregar")) {
                usuario = Usuario(
                    binding.etUsuario.text.toString().trim(),
                    binding.etPais.text.toString().trim()
                )
                addUsuario(room, usuario)
            } else if (binding.btnAddUpdate.text.equals("actualizar")) {
                usuario.pais = binding.etPais.text.toString().trim()

                updateUsuario(room, usuario)
            }
        }
    }
    fun obtenerUsuarios(room: DBPrueba){
        lifecycleScope.launch {
            listaUsuarios   =   room.daoUsuario().obtenerUsuario()
            adaptador   =   AdaptadorUsuarios(listaUsuarios, this@MainActivity)
            binding.rvUsuarios.adapter  =   adaptador
        }
    }

    fun addUsuario(room: DBPrueba, usuario: Usuario){
        lifecycleScope.launch{
            room.daoUsuario().addUsuario(usuario)
            obtenerUsuarios(room)
            clearFields()
        }
    }

    fun updateUsuario(room: DBPrueba, usuario: Usuario){
        lifecycleScope.launch {
            room.daoUsuario().updateUsuario(usuario.usuario, usuario.pais)
            obtenerUsuarios(room)
            clearFields()
        }
    }

    fun clearFields(){
        usuario.usuario =   ""
        usuario.pais    =   ""
        binding.etUsuario.setText("")
        binding.etPais.setText("")

        if (binding.btnAddUpdate.text.equals("actualizar")){
            binding.btnAddUpdate.setText("agregar")
            binding.etUsuario.isEnabled =   true
        }
    }

    override fun onEditItemClick(usuario: Usuario) {
        binding.btnAddUpdate.setText("actualizar")
        binding.etUsuario.isEnabled =   false
        this.usuario    =   usuario
        binding.etUsuario.setText(this.usuario.usuario)
        binding.etPais.setText(this.usuario.pais)
    }

    override fun onDeleteItemClick(usuario: Usuario) {
        lifecycleScope.launch{
            room.daoUsuario().deleteUsuario(usuario.usuario)
            adaptador.notifyDataSetChanged()
            obtenerUsuarios(room)
        }
    }
}