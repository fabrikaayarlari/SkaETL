<template>
  <v-card class="mb-5">
    <v-card-title>
      <div>
        <div class="headline">Select your transformations</div>
        <div><span class="grey--text"><small>Optional</small></span></div>
      </div>
    </v-card-title>
    <v-card-text>
      <v-dialog v-model="dialog" max-width="500px">
        <v-btn color="success" slot="activator">add Transformation
          <v-icon>add</v-icon>
        </v-btn>
        <v-card>
          <v-card-title>
            <span class="headline">{{ formTitle }}</span>
          </v-card-title>
          <v-card-text>
            <v-layout row wrap>
              <v-select label="Type Transformation" v-model="editedItem.typeTransformation" v-bind:items="type"
                        max-height="600"/>
            </v-layout>

            <v-layout row wrap v-show="isComposeField()">
              <v-text-field label="Key Field"
                            v-model="editedItem.parameterTransformation.composeField.key" required></v-text-field>
              <v-text-field label="Value Field"
                            v-model="editedItem.parameterTransformation.composeField.value" required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isDateField()">
              <v-text-field label="Key Field"
                            v-model="editedItem.parameterTransformation.formatDateValue.keyField"
                            required></v-text-field>
              <v-text-field label="Source Format"
                            v-model="editedItem.parameterTransformation.formatDateValue.srcFormat"
                            required></v-text-field>
              <v-text-field label="Target Format"
                            v-model="editedItem.parameterTransformation.formatDateValue.targetFormat"
                            required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isLookupList()">
              <v-text-field label="Limit on field (Optional) "
                            v-model="editedItem.parameterTransformation.keyField"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isLookupList()">
              <v-text-field label="Replace value" v-model="replaceValue" required></v-text-field>
              <v-icon slot="divider" color="blue">forward</v-icon>
              <v-text-field label="new value" v-model="replaceNewValue" required></v-text-field>
              <v-btn color="primary" v-on:click.native="addItemToLookupList">Add</v-btn>
            </v-layout>
            <v-layout row wrap v-show="isLookupList()">
              <v-flex v-for="item in listLookup">
                <v-chip color="orange" text-color="white" close @input="deleteItemToLookupList(item)">
                  {{item.oldValue}}-{{item.newValue}}
                </v-chip>
              </v-flex>
            </v-layout>

            <v-layout row wrap v-show="isHash()">
              <v-text-field label="Field"
                            v-model="editedItem.parameterTransformation.processHashData.field" required></v-text-field>
              <v-select label="Type Hash" v-model="editedItem.parameterTransformation.processHashData.typeHash"
                        v-bind:items="typeHash" max-height="600" required/>
            </v-layout>

            <v-layout row wrap v-show="isKeyField()">
              <v-text-field label="Field "
                            v-model="editedItem.parameterTransformation.keyField" required></v-text-field>
            </v-layout>

            <v-layout row wrap v-show="isLookupExternal()">
              <v-text-field label="Url"
                            v-model="editedItem.parameterTransformation.externalHTTPData.url"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isLookupExternal()">
              <v-select label="METHOD" v-model="editedItem.parameterTransformation.externalHTTPData.httpMethod"
                        v-bind:items="methodCall"/>
              <v-text-field type="number" label="Refresh in seconds"
                            v-model="editedItem.parameterTransformation.externalHTTPData.refresh"></v-text-field>
              <v-text-field label="Limit on field (Optional) "
                            v-model="editedItem.parameterTransformation.keyField"></v-text-field>
            </v-layout>
            <v-layout row wrap v-show="isLookupExternal()">
              <v-text-field label="body with POST"
                            v-model="editedItem.parameterTransformation.externalHTTPData.body"></v-text-field>
            </v-layout>
          </v-card-text>
          <v-card-actions>
            <v-spacer></v-spacer>
            <v-btn color="blue darken-1" flat @click.native="close">Cancel</v-btn>
            <v-btn color="blue darken-1" flat @click.native="save">Save</v-btn>
          </v-card-actions>

        </v-card>

      </v-dialog>
      <v-data-table :headers="headers" :items="processTransformations" hide-actions>
        <template slot="items" slot-scope="props">
          <td>{{props.item.typeTransformation}}</td>
          <td>{{formatField(props.item)}}</td>
          <td class="justify-center layout px-0">
            <v-btn icon class="mx-0" @click="editItem(props.item)">
              <v-icon color="teal">edit</v-icon>
            </v-btn>
            <v-btn icon class="mx-0" @click="deleteItem(props.item)">
              <v-icon color="pink">delete</v-icon>
            </v-btn>
          </td>
        </template>
      </v-data-table>
    </v-card-text>
    <v-card-actions>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('previousStep')">
        <v-icon>navigate_before</v-icon>
        Previous
      </v-btn>
      <v-btn color="primary" style="width: 120px" @click.native="$emit('nextStep')">Next
        <v-icon>navigate_next</v-icon>
      </v-btn>
    </v-card-actions>
  </v-card>
</template>


<script>
  export default {
    props: {
      processTransformations: {
        type: Array,
        required: true
      }
    },
    data: function () {
      return {
        dialog: false,
        editedItem: {
          "parameterTransformation": {
            "composeField": {"key": "", "value": ""},
            "formatDateValue": {"keyField": "", "srcFormat": "", "targetFormat": ""},
            "keyField": "",
            "mapLookup": [],
            "externalHTTPData": {"url": "http://url:port", "refresh": "10", "httpMethod": "GET", "body": ""},
            "processHashData": {"field": "", "typeHash": "SHA256"}
          }
        },
        editedIndex: -1,
        headers: [
          {text: 'Type', value: 'typeTransformation'},
          {text: 'Field', value: 'parameterTransformation.keyField'},
          {text: 'Actions', value: 'typeParser', sortable: false}
        ],
        defaultItem: {
          "parameterTransformation": {
            "composeField": {"key": "", "value": ""},
            "formatDateValue": {"keyField": "", "srcFormat": "", "targetFormat": ""},
            "keyField": "",
            "mapLookup": [],
            "externalHTTPData": {"url": "http://url:port", "refresh": "10", "httpMethod": "GET", "body": ""},
            "processHashData": {"field": "", "typeHash": "SHA256"}
          }
        },
        methodCall: ["GET", "POST"],
        typeHash: ["MURMUR3", "SHA256"],
        type: ["ADD_FIELD", "DELETE_FIELD", "RENAME_FIELD", "FORMAT_DATE", "FORMAT_BOOLEAN", "FORMAT_GEOPOINT",
          "FORMAT_DOUBLE", "FORMAT_LONG", "FORMAT_IP", "LOOKUP_LIST", "LOOKUP_EXTERNAL", "HASH", "ADD_GEO_LOCALISATION",
          "CAPITALIZE", "UNCAPITALIZE", "UPPER_CASE", "LOWER_CASE", "SWAP_CASE", "TRIM", "FORMAT_EMAIL"],
        replaceValue: '',
        replaceNewValue: '',
        listLookup: []
      }
    },
    computed: {
      formTitle() {
        return this.editedIndex === -1 ? 'New Item' : 'Edit Item';
      }
    },
    methods: {
      isComposeField() {
        return this.editedItem.typeTransformation == "ADD_FIELD" || this.editedItem.typeTransformation == "RENAME_FIELD";
      },
      isDateField() {
        return this.editedItem.typeTransformation == "FORMAT_DATE";
      },
      isLookupList() {
        return this.editedItem.typeTransformation == "LOOKUP_LIST";
      },
      isLookupExternal() {
        return this.editedItem.typeTransformation == "LOOKUP_EXTERNAL";
      },
      isHash() {
        return this.editedItem.typeTransformation == "HASH";
      },
      isKeyField() {
        var value = this.editedItem.typeTransformation;
        return value == "DELETE_FIELD" || value == "FORMAT_BOOLEAN" || value == "FORMAT_GEOPOINT" || value == "FORMAT_DOUBLE" || value == "FORMAT_LONG" || value == "FORMAT_IP" || value == "ADD_GEO_LOCALISATION" || value == "CAPITALIZE" || value == "UNCAPITALIZE" || value == "LOWER_CASE" || value == "UPPER_CASE" || value == "SWAP_CASE" || value == "TRIM" || value == "FORMAT_EMAIL";
      },
      formatField(item) {
        if (item.typeTransformation == "ADD_FIELD" || item.typeTransformation == "RENAME_FIELD") {
          return item.parameterTransformation.composeField.key;
        } else if (item.typeTransformation == "HASH") {
          return item.parameterTransformation.processHashData.field;
        } else {
          return item.parameterTransformation.keyField;
        }
      },
      close() {
        this.dialog = false;
        this.editedItem = _.cloneDeep(this.defaultItem);
        this.editedIndex = -1;
        this.lookupList = [];
        this.replaceValue = '';
        this.replaceNewValue = '';
      },
      editItem(item) {
        this.editedIndex = this.processTransformations.indexOf(item);
        this.editedItem = _.cloneDeep(item);
        this.dialog = true;
        for (var i = 0; i < this.editedItem.parameterTransformation.mapLookup.length; i++) {
            console.log(this.editedItem.parameterTransformation.mapLookup[i]);
        }
      },
      deleteItem(item) {
        var index = this.processTransformations.indexOf(item);
        confirm('Are you sure you want to delete this item?') && this.processTransformations.splice(index, 1);
      },

      save() {
        if (this.viewLookupList) {
          var result = {};
          for (var i = 0; i < this.listLookup.length; i++) {
            var itemLookup = this.listLookup[i];
            result[itemLookup.oldValue] = itemLookup.newValue;
          }
          this.editedItem.parameterTransformation.mapLookup = result;
        }
        if (this.editedIndex > -1) {
          Object.assign(this.processTransformations[this.editedIndex], this.editedItem);
        } else {
          this.processTransformations.push(this.editedItem);
        }
        this.close();
      },
      deleteItemToLookupList(item){
        this.listLookup=this.listLookup.filter(e => e !== item);
      },
      addItemToLookupList(){
        this.listLookup.push({oldValue: this.replaceValue, newValue: this.replaceNewValue});
      }
    }
  }
</script>
