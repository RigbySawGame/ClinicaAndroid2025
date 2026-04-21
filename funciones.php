<?php 
    function GuardarPaciente($nombres, $apaterno, $amaterno, $edad, $sexo, $altura, $peso, $detalle) {
        require_once("conexion.php");

        $sql = "INSERT INTO paciente() VALUES(
        null,
        '$nombres',
        '$apaterno',
        '$amaterno',
        '$edad',
        '$sexo',
        '$altura',
        '$peso',
        '$detalle'
        )";

        $result = mysqli_query($con,$sql);

        if(!$result) {
            $mensaje = "No se pudo registrar.";
        } else {
            $mensaje = "Se registró correctamente";
        }

        mysqli_close($con);

        return $mensaje;
    }

    function MostrarPaciente() {
        require_once("conexion.php");

        $sql = "SELECT * FROM paciente";

        $result = mysqli_query($con,$sql);

        $datos = array();
        while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)){
            $datos[] =  $row; 
        }

        mysqli_close($con);

        return $datos;
    }

    function EliminarPaciente($id) {
        require_once("conexion.php");

        $sql = "DELETE FROM paciente WHERE id = '$id'";

        $result = mysqli_query($con,$sql);

        if(!$result) {
            $mensaje = "No se pudo eliminar.";
        } else {
            $mensaje = "Se eliminó correctamente";
        }

        mysqli_close($con);

        return $mensaje;
    }

    function ConsultarPaciente($id)
    {
        require_once("conexion.php");

        $sql = "SELECT * FROM paciente WHERE id = '$id'";

        $result = mysqli_query($con,$sql);

        $datos = array();
        while($row = mysqli_fetch_array($result,MYSQLI_ASSOC)) {
            $datos[] =  $row; 
        }

        mysqli_close($con);

        return $datos;
    }

    function ActualizarPaciente($id, $nombres, $apaterno, $amaterno, $edad, $sexo, $altura, $peso, $detalle) {

        require_once("conexion.php");

        $sql = "UPDATE paciente SET 
        nombres = '$nombres',
        ape_paterno = '$apaterno',
        ape_materno = '$amaterno',
        edad = '$edad',
        sexo = '$sexo',
        altura = '$altura',
        peso = '$peso',
        detalle = '$detalle'
        WHERE id = '$id'
        ";

        $result = mysqli_query($con,$sql);

        if(!$result) {
            $mensaje = "No se pudo actualizar.";
        } else {
            $mensaje = "Se actualizó correctamente";
        }

        mysqli_close($con);

        return $mensaje;
    }
?>