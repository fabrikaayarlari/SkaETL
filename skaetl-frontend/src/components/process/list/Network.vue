<template>
  <v-container fluid grid-list-md >
    <v-card>
      <v-card-title class="card-title">Map of consumer processes</v-card-title>

        <v-switch v-model="visibles" label="Consumer processes" color="success" value="consumer" hide-details></v-switch>
        <v-switch v-model="visibles" label="Metric processes" color="red" value="metric" hide-details></v-switch>

      <d3-network :net-nodes="nodes" :net-links="links" :options="options" :link-cb="lcb"/>

      <v-layout row >
        <v-flex xs12 sm12 md12 >
          <v-alert v-model="viewError" xs12 sm12 md12  color="error" icon="warning" value="true" dismissible>
            {{ msgError }}
          </v-alert>
        </v-flex>
      </v-layout>
      <v-card-actions>
        <v-btn v-show="this.source =='consumer'" color="primary" flat href="/process/list">Return to consumer process list</v-btn>
        <v-btn v-show="this.source =='metric'" color="primary" flat href="/metric/list">Return to metric process list</v-btn>
      </v-card-actions>
    </v-card>
  </v-container>
</template>

<style src="vue-d3-network/dist/vue-d3-network.css"></style>
<style>
  .card-title {
    color: #757575;
    font-size: 22px;
    font-weight: bold;
  }
  #m-end path, #m-start{
    fill: rgba(18, 120, 98, 0.8);
  }
  .node-label{
    font-size: 12;
  }
  .link-label{
    fill: black;
    transform: translate(0,4);
    font-size: 11;
  }
</style>

<script>
  import D3Network from 'vue-d3-network';

  export default{
    components: {
      D3Network
    },
    data () {
         return {
           viewError : false,
           msgError : '',
           nodes: [],
           links: [],
           consumerNodes: [],
           consumerLinks: [],
           consumerNodes: [],
           consumerLinks: [],
           options: {
            force: 15000,
            nodeSize: 20,
            nodeLabels: true,
            linkLabels: true,
            linkWidth: 2,
           },
           visibles: ['consumer','metric'],
           source: ''
      }
    },
    mounted() {
         this.$http.get('/process/network').then(response => {
             var network = response.data;
             this.consumerNodes=network.consumerNodeList;
             this.consumerLinks=network.consumerLinksList;
             this.metricNodes=network.metricNodeList;
             this.metricLinks=network.metricLinksList;

             this.source = this.$route.query.source;
             if (this.source == "metric")
               this.visibles = ['metric'];
             else
               this.visibles = ['consumer'];

         }, response => {
           this.viewError=true;
           this.msgError = "Error during call service";
         });
    },
    watch: {
      visibles: function () {
        this.nodes=[];
        this.links=[];
        if (this.visibles.includes("metric")) {
          this.nodes = this.nodes.concat(this.metricNodes);
          this.links = this.links.concat(this.metricLinks);
        }
        if (this.visibles.includes("consumer")) {
          this.nodes = this.nodes.concat(this.consumerNodes);
          this.links = this.links.concat(this.consumerLinks);
        }
      }
    },
    methods: {
      lcb (link) {
        link._svgAttrs = { 'marker-end': 'url(#m-end)'}
        return link
      }
    }
  }
</script>
