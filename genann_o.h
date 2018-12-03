class genann_o : public object {
  genann *_network;
  number_t num_inputs;
  number_t num_hidden_layers;
  number_t num_hidden;
  number_t _num_outputs;

public:
  
  type_t type() const final { return type_id<genann_o>; }

#if !defined(FERRET_DISABLE_STD_OUT)
  void stream_console() const {
    runtime::print("genann<");
    runtime::print(num_inputs);
    runtime::print(" ");
    runtime::print(num_hidden_layers);
    runtime::print(" ");
    runtime::print(num_hidden);
    runtime::print(" ");
    runtime::print(_num_outputs);
    runtime::print(">");
  }
#endif

  explicit genann_o(number_t i, number_t hl, number_t h, number_t o)
    : num_inputs(i), num_hidden_layers(hl), num_hidden(h), _num_outputs(o) {
    _network =
      genann_init(num_inputs, num_hidden_layers, num_hidden, _num_outputs);
  }
  explicit genann_o(genann *ann) : _network(ann) {
    num_inputs = _network->inputs;
    num_hidden_layers = _network->hidden_layers;
    num_hidden = _network->hidden;
    _num_outputs = _network->outputs;
  }
  genann *network() const { return _network; }
  number_t num_outputs() const { return _num_outputs; }
  ~genann_o() { genann_free(_network); }
};
